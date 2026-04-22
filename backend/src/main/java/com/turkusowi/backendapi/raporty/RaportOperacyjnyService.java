package com.turkusowi.backendapi.raporty;

import com.turkusowi.backendapi.common.ConflictException;
import com.turkusowi.backendapi.common.NotFoundException;
import com.turkusowi.backendapi.uzytkownicy.Uzytkownik;
import com.turkusowi.backendapi.uzytkownicy.UzytkownikService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RaportOperacyjnyService {

    private final RaportOperacyjnyRepository raportOperacyjnyRepository;
    private final UzytkownikService uzytkownikService;

    public RaportOperacyjnyService(
            RaportOperacyjnyRepository raportOperacyjnyRepository,
            UzytkownikService uzytkownikService
    ) {
        this.raportOperacyjnyRepository = raportOperacyjnyRepository;
        this.uzytkownikService = uzytkownikService;
    }

    public List<RaportOperacyjnyResponse> findAll(LocalDate dataRaportu, String typRaportu) {
        return raportOperacyjnyRepository.findAll(Sort.by(Sort.Direction.DESC, "dataRaportu"))
                .stream()
                .filter(raport -> dataRaportu == null || raport.getDataRaportu().equals(dataRaportu))
                .filter(raport -> typRaportu == null || raport.getTypRaportu().equalsIgnoreCase(typRaportu))
                .map(this::mapToResponse)
                .toList();
    }

    public RaportOperacyjnyResponse findById(Integer id) {
        return mapToResponse(getRequiredEntity(id));
    }

    public RaportOperacyjnyResponse create(UpsertRaportOperacyjnyRequest request) {
        RaportOperacyjny raport = new RaportOperacyjny();
        apply(raport, request);
        return mapToResponse(raportOperacyjnyRepository.save(raport));
    }

    public RaportOperacyjnyResponse update(Integer id, UpsertRaportOperacyjnyRequest request) {
        RaportOperacyjny raport = getRequiredEntity(id);
        apply(raport, request);
        return mapToResponse(raportOperacyjnyRepository.save(raport));
    }

    public void delete(Integer id) {
        RaportOperacyjny raport = getRequiredEntity(id);
        raportOperacyjnyRepository.delete(raport);
    }

    public RaportOperacyjny getRequiredEntity(Integer id) {
        return raportOperacyjnyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono raportu operacyjnego o id=" + id));
    }

    private void apply(RaportOperacyjny raport, UpsertRaportOperacyjnyRequest request) {
        Uzytkownik sekretarz = uzytkownikService.getRequiredEntity(request.sekretarzId());
        validateRole(sekretarz);

        raport.setDataRaportu(request.dataRaportu());
        raport.setSekretarz(sekretarz);
        raport.setTypRaportu(request.typRaportu().trim());
        raport.setUwagi(trimToNull(request.uwagi()));
    }

    private void validateRole(Uzytkownik sekretarz) {
        String roleName = sekretarz.getRola().getNazwa();
        boolean allowed = "SEKRETARZ".equalsIgnoreCase(roleName) || "ADMIN".equalsIgnoreCase(roleName);
        if (!allowed) {
            throw new ConflictException("Raport moze utworzyc tylko sekretariat lub administrator.");
        }
    }

    private RaportOperacyjnyResponse mapToResponse(RaportOperacyjny raport) {
        return new RaportOperacyjnyResponse(
                raport.getId(),
                raport.getDataRaportu(),
                raport.getSekretarz().getId(),
                raport.getSekretarz().getImie(),
                raport.getSekretarz().getNazwisko(),
                raport.getTypRaportu(),
                raport.getUwagi()
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
