package com.turkusowi.backendapi.zwierzeta;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zwierzeta")
public class ZwierzeController {

    private final ZwierzeService zwierzeService;

    public ZwierzeController(ZwierzeService zwierzeService) {
        this.zwierzeService = zwierzeService;
    }

    @GetMapping
    public List<ZwierzeResponse> findAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer rasaId,
            @RequestParam(required = false) String typZwierzecia
    ) {
        return zwierzeService.findAll(status, rasaId, typZwierzecia);
    }

    @GetMapping("/publiczne")
    public List<ZwierzePublicResponse> findPubliczne() {
        return zwierzeService.findPubliczne();
    }

    @GetMapping("/{id}")
    public ZwierzeResponse findById(@PathVariable Integer id) {
        return zwierzeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ZwierzeResponse create(@Valid @RequestBody UpsertZwierzeRequest request) {
        return zwierzeService.create(request);
    }

    @PutMapping("/{id}")
    public ZwierzeResponse update(@PathVariable Integer id, @Valid @RequestBody UpsertZwierzeRequest request) {
        return zwierzeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        zwierzeService.delete(id);
    }
}
