package com.turkusowi.backendapi.historia;

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
public class HistoriaMedycznaService {

    private static final String ROLA_WOLONTARIUSZ = "WOLONTARIUSZ";

    private final HistoriaMedycznaRepository historiaMedycznaRepository;
    private final ZwierzeService zwierzeService;
    private final UzytkownikService uzytkownikService;

    public HistoriaMedycznaService(
            HistoriaMedycznaRepository historiaMedycznaRepository,
            ZwierzeService zwierzeService,
            UzytkownikService uzytkownikService
    ) {
        this.historiaMedycznaRepository = historiaMedycznaRepository;
        this.zwierzeService = zwierzeService;
        this.uzytkownikService = uzytkownikService;
    }

    public List<HistoriaMedycznaResponse> findAll(Integer zwierzeId, Integer pracownikId) {
        return historiaMedycznaRepository.findAll(Sort.by(Sort.Direction.DESC, "dataWpisu"))
                .stream()
                .filter(wpis -> zwierzeId == null || wpis.getZwierze().getId().equals(zwierzeId))
                .filter(wpis -> pracownikId == null || wpis.getPracownik().getId().equals(pracownikId))
                .map(this::mapToResponse)
                .toList();
    }

    public HistoriaMedycznaResponse findById(Integer id) {
        return mapToResponse(getRequiredEntity(id));
    }

    public HistoriaMedycznaResponse create(UpsertHistoriaMedycznaRequest request) {
        HistoriaMedyczna wpis = new HistoriaMedyczna();
        apply(wpis, request);
        return mapToResponse(historiaMedycznaRepository.save(wpis));
    }

    public HistoriaMedycznaResponse update(Integer id, UpsertHistoriaMedycznaRequest request) {
        HistoriaMedyczna wpis = getRequiredEntity(id);
        apply(wpis, request);
        return mapToResponse(historiaMedycznaRepository.save(wpis));
    }

    public void delete(Integer id) {
        HistoriaMedyczna wpis = getRequiredEntity(id);
        historiaMedycznaRepository.delete(wpis);
    }

    public HistoriaMedyczna getRequiredEntity(Integer id) {
        return historiaMedycznaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono wpisu historii medycznej o id=" + id));
    }

    private void apply(HistoriaMedyczna wpis, UpsertHistoriaMedycznaRequest request) {
        Zwierze zwierze = zwierzeService.getRequiredEntity(request.zwierzeId());
        Uzytkownik pracownik = uzytkownikService.getRequiredEntity(request.pracownikId());

        if (ROLA_WOLONTARIUSZ.equalsIgnoreCase(pracownik.getRola().getNazwa())) {
            throw new ConflictException("Wpis medyczny moze dodac tylko pracownik schroniska lub sekretariat.");
        }

        wpis.setZwierze(zwierze);
        wpis.setPracownik(pracownik);
        wpis.setOpisZabiegu(request.opisZabiegu().trim());
        if (request.dataWpisu() != null) {
            wpis.setDataWpisu(request.dataWpisu());
        }
    }

    private HistoriaMedycznaResponse mapToResponse(HistoriaMedyczna wpis) {
        return new HistoriaMedycznaResponse(
                wpis.getId(),
                wpis.getZwierze().getId(),
                wpis.getZwierze().getImie(),
                wpis.getPracownik().getId(),
                wpis.getPracownik().getImie(),
                wpis.getPracownik().getNazwisko(),
                wpis.getOpisZabiegu(),
                wpis.getDataWpisu()
        );
    }
}
