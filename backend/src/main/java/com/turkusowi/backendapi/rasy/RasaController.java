package com.turkusowi.backendapi.rasy;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rasy")
public class RasaController {

    private final RasaRepository rasaRepository;

    public RasaController(RasaRepository rasaRepository) {
        this.rasaRepository = rasaRepository;
    }

    @GetMapping
    public List<Rasa> findAll() {
        return rasaRepository.findAll();
    }

    @PostMapping
    public Rasa create(@RequestBody Rasa rasa) {
        return rasaRepository.save(rasa);
    }
}