package com.turkusowi.backendapi.rasy;

import com.turkusowi.backendapi.common.ConflictException;
import com.turkusowi.backendapi.common.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RasaService {

    private final RasaRepository rasaRepository;

    public RasaService(RasaRepository rasaRepository) {
        this.rasaRepository = rasaRepository;
    }

    public List<RasaResponse> findAll() {
        return rasaRepository.findAll(Sort.by(Sort.Direction.ASC, "typZwierzecia", "rasa"))
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public RasaResponse findById(Integer id) {
        return mapToResponse(getRequiredEntity(id));
    }

    public RasaResponse create(UpsertRasaRequest request) {
        validateUniqueBreed(request.rasa(), null);

        Rasa rasa = new Rasa();
        apply(rasa, request);
        return mapToResponse(rasaRepository.save(rasa));
    }

    public RasaResponse update(Integer id, UpsertRasaRequest request) {
        Rasa rasa = getRequiredEntity(id);
        validateUniqueBreed(request.rasa(), id);
        apply(rasa, request);
        return mapToResponse(rasaRepository.save(rasa));
    }

    public void delete(Integer id) {
        Rasa rasa = getRequiredEntity(id);
        rasaRepository.delete(rasa);
    }

    public Rasa getRequiredEntity(Integer id) {
        return rasaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono rasy o id=" + id));
    }

    private void validateUniqueBreed(String breedName, Integer currentId) {
        rasaRepository.findByRasaIgnoreCase(breedName)
                .filter(rasa -> !rasa.getId().equals(currentId))
                .ifPresent(rasa -> {
                    throw new ConflictException("Rasa '" + breedName + "' juz istnieje.");
                });
    }

    private void apply(Rasa rasa, UpsertRasaRequest request) {
        rasa.setTypZwierzecia(request.typZwierzecia().trim());
        rasa.setRasa(request.rasa().trim());
    }

    private RasaResponse mapToResponse(Rasa rasa) {
        return new RasaResponse(rasa.getId(), rasa.getTypZwierzecia(), rasa.getRasa());
    }
}
