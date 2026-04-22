package com.turkusowi.backendapi.uzytkownicy;

import com.turkusowi.backendapi.common.ConflictException;
import com.turkusowi.backendapi.common.NotFoundException;
import com.turkusowi.backendapi.role.Rola;
import com.turkusowi.backendapi.role.RolaService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UzytkownikService {

    private final UzytkownikRepository uzytkownikRepository;
    private final RolaService rolaService;

    public UzytkownikService(UzytkownikRepository uzytkownikRepository, RolaService rolaService) {
        this.uzytkownikRepository = uzytkownikRepository;
        this.rolaService = rolaService;
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
        uzytkownik.setHasloHash(request.hasloHash().trim());
        uzytkownik.setImie(request.imie().trim());
        uzytkownik.setNazwisko(request.nazwisko().trim());
        uzytkownik.setRola(rola);
        uzytkownik.setCzyAktywny(request.czyAktywny());
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

    public LoginResponse login(LoginRequest request) {
        Uzytkownik uzytkownik = uzytkownikRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new NotFoundException("Niepoprawny email lub haslo"));

        if(!uzytkownik.getHasloHash().equals(request.haslo())) {
            throw new ConflictException("Niepoprawny email lub haslo");
        }

        if(!uzytkownik.isCzyAktywny()) {
            throw new ConflictException("Konto uzytkownika jest nieaktywne");
        }

        uzytkownik.setOstatnieLogowanie(java.time.LocalDateTime.now());
        uzytkownikRepository.save(uzytkownik);

        return new LoginResponse("mock-token", mapToResponse(uzytkownik));
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
