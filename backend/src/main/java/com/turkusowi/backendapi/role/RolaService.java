package com.turkusowi.backendapi.role;

import com.turkusowi.backendapi.common.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolaService {

    private final RolaRepository rolaRepository;

    public RolaService(RolaRepository rolaRepository) {
        this.rolaRepository = rolaRepository;
    }

    public List<RolaResponse> findAll() {
        return rolaRepository.findAll(Sort.by(Sort.Direction.ASC, "nazwa"))
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Rola getRequiredEntity(Integer id) {
        return rolaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono roli o id=" + id));
    }

    public Rola getRequiredVolunteerRole() {
        return rolaRepository.findByNazwaIgnoreCase("WOLONTARIUSZ")
                .orElseThrow(() -> new NotFoundException("Nie znaleziono roli WOLONTARIUSZ."));
    }

    private RolaResponse mapToResponse(Rola rola) {
        return new RolaResponse(rola.getId(), rola.getNazwa());
    }
}
