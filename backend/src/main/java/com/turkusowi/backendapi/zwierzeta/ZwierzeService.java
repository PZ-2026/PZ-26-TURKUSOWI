package com.turkusowi.backendapi.zwierzeta;

import com.turkusowi.backendapi.common.NotFoundException;
import com.turkusowi.backendapi.rasy.Rasa;
import com.turkusowi.backendapi.rasy.RasaService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZwierzeService {

    private static final String STATUS_DO_ADOPCJI = "DO ADOPCJI";

    private final ZwierzeRepository zwierzeRepository;
    private final RasaService rasaService;

    public ZwierzeService(ZwierzeRepository zwierzeRepository, RasaService rasaService) {
        this.zwierzeRepository = zwierzeRepository;
        this.rasaService = rasaService;
    }

    public List<ZwierzeResponse> findAll(String status, Integer rasaId, String typZwierzecia) {
        return zwierzeRepository.findAll(Sort.by(Sort.Direction.ASC, "imie"))
                .stream()
                .filter(zwierze -> status == null || equalsIgnoreCase(zwierze.getStatus(), status))
                .filter(zwierze -> rasaId == null || hasRasaId(zwierze, rasaId))
                .filter(zwierze -> typZwierzecia == null || hasAnimalType(zwierze, typZwierzecia))
                .map(this::mapToResponse)
                .toList();
    }

    public List<ZwierzePublicResponse> findPubliczne() {
        return zwierzeRepository.findAll(Sort.by(Sort.Direction.ASC, "imie"))
                .stream()
                .filter(zwierze -> equalsIgnoreCase(zwierze.getStatus(), STATUS_DO_ADOPCJI))
                .map(this::mapToPublicResponse)
                .toList();
    }

    public ZwierzeResponse findById(Integer id) {
        return mapToResponse(getRequiredEntity(id));
    }

    public ZwierzeResponse create(UpsertZwierzeRequest request) {
        Zwierze zwierze = new Zwierze();
        apply(zwierze, request);
        return mapToResponse(zwierzeRepository.save(zwierze));
    }

    public ZwierzeResponse update(Integer id, UpsertZwierzeRequest request) {
        Zwierze zwierze = getRequiredEntity(id);
        apply(zwierze, request);
        return mapToResponse(zwierzeRepository.save(zwierze));
    }

    public void delete(Integer id) {
        Zwierze zwierze = getRequiredEntity(id);
        zwierzeRepository.delete(zwierze);
    }

    public Zwierze getRequiredEntity(Integer id) {
        return zwierzeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono zwierzecia o id=" + id));
    }

    private void apply(Zwierze zwierze, UpsertZwierzeRequest request) {
        Rasa rasa = request.rasaId() == null ? null : rasaService.getRequiredEntity(request.rasaId());

        zwierze.setImie(request.imie().trim());
        zwierze.setWiek(request.wiek());
        zwierze.setWaga(request.waga());
        zwierze.setPlec(trimToNull(request.plec()));
        zwierze.setRasa(rasa);
        zwierze.setStatus(trimToNull(request.status()));
        zwierze.setOpis(trimToNull(request.opis()));
        zwierze.setCharakter(trimToNull(request.charakter()));
        zwierze.setPreferencjeZywieniowe(trimToNull(request.preferencjeZywieniowe()));
    }

    private ZwierzeResponse mapToResponse(Zwierze zwierze) {
        return new ZwierzeResponse(
                zwierze.getId(),
                zwierze.getImie(),
                zwierze.getWiek(),
                zwierze.getWaga(),
                zwierze.getPlec(),
                zwierze.getRasa() != null ? zwierze.getRasa().getId() : null,
                zwierze.getRasa() != null ? zwierze.getRasa().getRasa() : null,
                zwierze.getRasa() != null ? zwierze.getRasa().getTypZwierzecia() : null,
                zwierze.getStatus(),
                zwierze.getOpis(),
                zwierze.getCharakter(),
                zwierze.getPreferencjeZywieniowe(),
                zwierze.getDataModyfikacji()
        );
    }

    private ZwierzePublicResponse mapToPublicResponse(Zwierze zwierze) {
        return new ZwierzePublicResponse(
                zwierze.getId(),
                zwierze.getImie(),
                zwierze.getWiek(),
                zwierze.getRasa() != null ? zwierze.getRasa().getRasa() : null,
                zwierze.getRasa() != null ? zwierze.getRasa().getTypZwierzecia() : null,
                zwierze.getStatus(),
                zwierze.getOpis(),
                zwierze.getCharakter()
        );
    }

    private boolean hasRasaId(Zwierze zwierze, Integer rasaId) {
        return zwierze.getRasa() != null && zwierze.getRasa().getId().equals(rasaId);
    }

    private boolean hasAnimalType(Zwierze zwierze, String typZwierzecia) {
        return zwierze.getRasa() != null
                && equalsIgnoreCase(zwierze.getRasa().getTypZwierzecia(), typZwierzecia);
    }

    private boolean equalsIgnoreCase(String left, String right) {
        return left != null && left.equalsIgnoreCase(right);
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
