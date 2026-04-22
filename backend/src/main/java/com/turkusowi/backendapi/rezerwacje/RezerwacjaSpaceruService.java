package com.turkusowi.backendapi.rezerwacje;

import com.turkusowi.backendapi.common.ConflictException;
import com.turkusowi.backendapi.common.NotFoundException;
import com.turkusowi.backendapi.uzytkownicy.Uzytkownik;
import com.turkusowi.backendapi.uzytkownicy.UzytkownikService;
import com.turkusowi.backendapi.zwierzeta.Zwierze;
import com.turkusowi.backendapi.zwierzeta.ZwierzeService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RezerwacjaSpaceruService {

    private static final String ROLA_WOLONTARIUSZ = "WOLONTARIUSZ";

    private final RezerwacjaSpaceruRepository rezerwacjaSpaceruRepository;
    private final UzytkownikService uzytkownikService;
    private final ZwierzeService zwierzeService;

    public RezerwacjaSpaceruService(
            RezerwacjaSpaceruRepository rezerwacjaSpaceruRepository,
            UzytkownikService uzytkownikService,
            ZwierzeService zwierzeService
    ) {
        this.rezerwacjaSpaceruRepository = rezerwacjaSpaceruRepository;
        this.uzytkownikService = uzytkownikService;
        this.zwierzeService = zwierzeService;
    }

    public List<RezerwacjaSpaceruResponse> findAll(Integer wolontariuszId, Integer zwierzeId, java.time.LocalDate dataSpaceru) {
        return rezerwacjaSpaceruRepository.findAll(Sort.by(Sort.Direction.ASC, "dataSpaceru", "godzinaStart"))
                .stream()
                .filter(rezerwacja -> wolontariuszId == null || rezerwacja.getWolontariusz().getId().equals(wolontariuszId))
                .filter(rezerwacja -> zwierzeId == null || rezerwacja.getZwierze().getId().equals(zwierzeId))
                .filter(rezerwacja -> dataSpaceru == null || rezerwacja.getDataSpaceru().equals(dataSpaceru))
                .map(this::mapToResponse)
                .toList();
    }

    public RezerwacjaSpaceruResponse findById(Integer id) {
        return mapToResponse(getRequiredEntity(id));
    }

    public RezerwacjaSpaceruResponse create(UpsertRezerwacjaSpaceruRequest request) {
        RezerwacjaSpaceru rezerwacja = new RezerwacjaSpaceru();
        apply(rezerwacja, request, null);
        return mapToResponse(rezerwacjaSpaceruRepository.save(rezerwacja));
    }

    public RezerwacjaSpaceruResponse update(Integer id, UpsertRezerwacjaSpaceruRequest request) {
        RezerwacjaSpaceru rezerwacja = getRequiredEntity(id);
        apply(rezerwacja, request, id);
        return mapToResponse(rezerwacjaSpaceruRepository.save(rezerwacja));
    }

    public void delete(Integer id) {
        RezerwacjaSpaceru rezerwacja = getRequiredEntity(id);
        rezerwacjaSpaceruRepository.delete(rezerwacja);
    }

    public RezerwacjaSpaceru getRequiredEntity(Integer id) {
        return rezerwacjaSpaceruRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono rezerwacji spaceru o id=" + id));
    }

    private void apply(RezerwacjaSpaceru rezerwacja, UpsertRezerwacjaSpaceruRequest request, Integer currentId) {
        validateTimeRange(request);

        Uzytkownik wolontariusz = uzytkownikService.getRequiredEntity(request.wolontariuszId());
        if (!ROLA_WOLONTARIUSZ.equalsIgnoreCase(wolontariusz.getRola().getNazwa())) {
            throw new ConflictException("Rezerwacje moze utworzyc tylko wolontariusz.");
        }

        Zwierze zwierze = zwierzeService.getRequiredEntity(request.zwierzeId());
        validateNoConflicts(zwierze.getId(), request, currentId);

        rezerwacja.setWolontariusz(wolontariusz);
        rezerwacja.setZwierze(zwierze);
        rezerwacja.setDataSpaceru(request.dataSpaceru());
        rezerwacja.setGodzinaStart(request.godzinaStart());
        rezerwacja.setGodzinaKoniec(request.godzinaKoniec());
        rezerwacja.setUwagi(trimToNull(request.uwagi()));
    }

    private void validateTimeRange(UpsertRezerwacjaSpaceruRequest request) {
        if (!request.godzinaKoniec().isAfter(request.godzinaStart())) {
            throw new ConflictException("Godzina zakonczenia musi byc pozniejsza niz godzina rozpoczecia.");
        }
    }

    private void validateNoConflicts(Integer zwierzeId, UpsertRezerwacjaSpaceruRequest request, Integer currentId) {
        boolean existsConflict = rezerwacjaSpaceruRepository
                .findByZwierzeIdAndDataSpaceru(zwierzeId, request.dataSpaceru())
                .stream()
                .filter(existing -> currentId == null || !existing.getId().equals(currentId))
                .anyMatch(existing ->
                        request.godzinaStart().isBefore(existing.getGodzinaKoniec())
                                && request.godzinaKoniec().isAfter(existing.getGodzinaStart())
                );

        if (existsConflict) {
            throw new ConflictException("Wybrany termin dla tego zwierzecia jest juz zajety.");
        }
    }

    private RezerwacjaSpaceruResponse mapToResponse(RezerwacjaSpaceru rezerwacja) {
        return new RezerwacjaSpaceruResponse(
                rezerwacja.getId(),
                rezerwacja.getWolontariusz().getId(),
                rezerwacja.getWolontariusz().getImie(),
                rezerwacja.getWolontariusz().getNazwisko(),
                rezerwacja.getZwierze().getId(),
                rezerwacja.getZwierze().getImie(),
                rezerwacja.getDataSpaceru(),
                rezerwacja.getGodzinaStart(),
                rezerwacja.getGodzinaKoniec(),
                rezerwacja.getUwagi(),
                rezerwacja.getDataZapisu()
        );
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
