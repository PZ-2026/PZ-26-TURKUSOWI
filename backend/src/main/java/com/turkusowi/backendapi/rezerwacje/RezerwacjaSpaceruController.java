package com.turkusowi.backendapi.rezerwacje;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rezerwacje-spacerow")
public class RezerwacjaSpaceruController {

    private final RezerwacjaSpaceruService rezerwacjaSpaceruService;

    public RezerwacjaSpaceruController(RezerwacjaSpaceruService rezerwacjaSpaceruService) {
        this.rezerwacjaSpaceruService = rezerwacjaSpaceruService;
    }

    @GetMapping
    public List<RezerwacjaSpaceruResponse> findAll(
            @RequestParam(required = false) Integer wolontariuszId,
            @RequestParam(required = false) Integer zwierzeId,
            @RequestParam(required = false) LocalDate dataSpaceru
    ) {
        return rezerwacjaSpaceruService.findAll(wolontariuszId, zwierzeId, dataSpaceru);
    }

    @GetMapping("/{id}")
    public RezerwacjaSpaceruResponse findById(@PathVariable Integer id) {
        return rezerwacjaSpaceruService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RezerwacjaSpaceruResponse create(@Valid @RequestBody UpsertRezerwacjaSpaceruRequest request) {
        return rezerwacjaSpaceruService.create(request);
    }

    @PutMapping("/{id}")
    public RezerwacjaSpaceruResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody UpsertRezerwacjaSpaceruRequest request
    ) {
        return rezerwacjaSpaceruService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        rezerwacjaSpaceruService.delete(id);
    }
}
