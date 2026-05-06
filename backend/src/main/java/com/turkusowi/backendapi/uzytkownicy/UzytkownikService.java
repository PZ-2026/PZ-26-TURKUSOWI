package com.turkusowi.backendapi.uzytkownicy;

import com.turkusowi.backendapi.common.ConflictException;
import com.turkusowi.backendapi.common.NotFoundException;
import com.turkusowi.backendapi.role.Rola;
import com.turkusowi.backendapi.role.RolaService;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UzytkownikService {

    private final UzytkownikRepository uzytkownikRepository;
    private final RolaService rolaService;
    private final PasswordEncoder passwordEncoder;

    public UzytkownikService(
            UzytkownikRepository uzytkownikRepository,
            RolaService rolaService,
            PasswordEncoder passwordEncoder
    ) {
        this.uzytkownikRepository = uzytkownikRepository;
        this.rolaService = rolaService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UzytkownikResponse> findAll(Integer rolaId, Boolean aktywny) {
        return uzytkownikRepository.findAll(Sort.by(Sort.Direction.ASC, "nazwisko", "imie"))
                .stream()
                .filter(uzytkownik -> rolaId == null || uzytkownik.getRola().getId().equals(rolaId))
                .filter(uzytkownik -> aktywny == null || uzytkownik.isCzyAktywny() == aktywny)
                .map(this::mapToResponse)
                .toList();
    }

    public UzytkownikResponse findById(Integer id) {
        return mapToResponse(getRequiredEntity(id));
    }

    public UzytkownikResponse create(CreateUzytkownikRequest request) {
        validateUniqueEmail(request.email(), null);

        Uzytkownik uzytkownik = new Uzytkownik();
        apply(uzytkownik, request);
        return mapToResponse(uzytkownikRepository.save(uzytkownik));
    }

    public UzytkownikResponse update(Integer id, UpdateUzytkownikRequest request) {
        Uzytkownik uzytkownik = getRequiredEntity(id);
        validateUniqueEmail(request.email(), id);
        apply(uzytkownik, request);
        return mapToResponse(uzytkownikRepository.save(uzytkownik));
    }

    public UzytkownikResponse updateStatus(Integer id, UpdateStatusUzytkownikaRequest request) {
        Uzytkownik uzytkownik = getRequiredEntity(id);
        uzytkownik.setCzyAktywny(request.czyAktywny());
        return mapToResponse(uzytkownikRepository.save(uzytkownik));
    }

    public void delete(Integer id) {
        Uzytkownik uzytkownik = getRequiredEntity(id);
        uzytkownikRepository.delete(uzytkownik);
    }

    public Uzytkownik getRequiredEntity(Integer id) {
        return uzytkownikRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono uzytkownika o id=" + id));
    }

    private void validateUniqueEmail(String email, Integer currentId) {
        boolean emailTaken = uzytkownikRepository.findAll().stream()
                .anyMatch(existing ->
                        existing.getEmail().equalsIgnoreCase(email)
                                && !existing.getId().equals(currentId)
                );

        if (emailTaken) {
            throw new ConflictException("Uzytkownik z adresem email '" + email + "' juz istnieje.");
        }
    }

    private void apply(Uzytkownik uzytkownik, BaseUzytkownikRequest request) {
        Rola rola = rolaService.getRequiredEntity(request.rolaId());

        uzytkownik.setEmail(request.email().trim());
        if (request.hasloHash() != null && !request.hasloHash().isBlank()) {
            uzytkownik.setHasloHash(normalizePasswordForStorage(request.hasloHash()));
        }
        uzytkownik.setImie(request.imie().trim());
        uzytkownik.setNazwisko(request.nazwisko().trim());
        uzytkownik.setRola(rola);
        uzytkownik.setCzyAktywny(request.czyAktywny());
    }

    private String normalizePasswordForStorage(String passwordValue) {
        String trimmed = passwordValue.trim();
        if (trimmed.startsWith("$2a$") || trimmed.startsWith("$2b$") || trimmed.startsWith("$2y$")) {
            return trimmed;
        }

        return passwordEncoder.encode(trimmed);
    }

    private UzytkownikResponse mapToResponse(Uzytkownik uzytkownik) {
        return new UzytkownikResponse(
                uzytkownik.getId(),
                uzytkownik.getEmail(),
                uzytkownik.getImie(),
                uzytkownik.getNazwisko(),
                uzytkownik.getRola().getId(),
                uzytkownik.getRola().getNazwa(),
                uzytkownik.isCzyAktywny(),
                uzytkownik.getOstatnieLogowanie(),
                uzytkownik.getDataUtworzenia(),
                uzytkownik.getDataModyfikacji()
        );
    }

    sealed interface BaseUzytkownikRequest permits CreateUzytkownikRequest, UpdateUzytkownikRequest {
        String email();

        String hasloHash();

        String imie();

        String nazwisko();

        Integer rolaId();

        boolean czyAktywny();
    }
}
