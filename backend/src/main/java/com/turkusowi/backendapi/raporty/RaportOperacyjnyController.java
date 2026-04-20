package com.turkusowi.backendapi.raporty;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/raporty-operacyjne")
public class RaportOperacyjnyController {

    private final RaportOperacyjnyService raportOperacyjnyService;

    public RaportOperacyjnyController(RaportOperacyjnyService raportOperacyjnyService) {
        this.raportOperacyjnyService = raportOperacyjnyService;
    }

    @GetMapping
    public List<RaportOperacyjnyResponse> findAll(
            @RequestParam(required = false) LocalDate dataRaportu,
            @RequestParam(required = false) String typRaportu
    ) {
        return raportOperacyjnyService.findAll(dataRaportu, typRaportu);
    }

    @GetMapping("/{id}")
    public RaportOperacyjnyResponse findById(@PathVariable Integer id) {
        return raportOperacyjnyService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RaportOperacyjnyResponse create(@Valid @RequestBody UpsertRaportOperacyjnyRequest request) {
        return raportOperacyjnyService.create(request);
    }

    @PutMapping("/{id}")
    public RaportOperacyjnyResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody UpsertRaportOperacyjnyRequest request
    ) {
        return raportOperacyjnyService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        raportOperacyjnyService.delete(id);
    }
}
