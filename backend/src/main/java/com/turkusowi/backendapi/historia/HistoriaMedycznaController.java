package com.turkusowi.backendapi.historia;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historia-medyczna")
public class HistoriaMedycznaController {

    private final HistoriaMedycznaService historiaMedycznaService;

    public HistoriaMedycznaController(HistoriaMedycznaService historiaMedycznaService) {
        this.historiaMedycznaService = historiaMedycznaService;
    }

    @GetMapping
    public List<HistoriaMedycznaResponse> findAll(
            @RequestParam(required = false) Integer zwierzeId,
            @RequestParam(required = false) Integer pracownikId
    ) {
        return historiaMedycznaService.findAll(zwierzeId, pracownikId);
    }

    @GetMapping("/{id}")
    public HistoriaMedycznaResponse findById(@PathVariable Integer id) {
        return historiaMedycznaService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HistoriaMedycznaResponse create(@Valid @RequestBody UpsertHistoriaMedycznaRequest request) {
        return historiaMedycznaService.create(request);
    }

    @PutMapping("/{id}")
    public HistoriaMedycznaResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody UpsertHistoriaMedycznaRequest request
    ) {
        return historiaMedycznaService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        historiaMedycznaService.delete(id);
    }
}
