package com.turkusowi.backendapi.rasy;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rasy")
public class RasaController {

    private final RasaService rasaService;

    public RasaController(RasaService rasaService) {
        this.rasaService = rasaService;
    }

    @GetMapping
    public List<RasaResponse> findAll() {
        return rasaService.findAll();
    }

    @GetMapping("/{id}")
    public RasaResponse findById(@PathVariable Integer id) {
        return rasaService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RasaResponse create(@Valid @RequestBody UpsertRasaRequest request) {
        return rasaService.create(request);
    }

    @PutMapping("/{id}")
    public RasaResponse update(@PathVariable Integer id, @Valid @RequestBody UpsertRasaRequest request) {
        return rasaService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        rasaService.delete(id);
    }
}
