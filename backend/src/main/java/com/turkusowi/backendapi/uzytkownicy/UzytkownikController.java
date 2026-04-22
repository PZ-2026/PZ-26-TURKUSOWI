package com.turkusowi.backendapi.uzytkownicy;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/uzytkownicy")
public class UzytkownikController {

    private final UzytkownikService uzytkownikService;

    public UzytkownikController(UzytkownikService uzytkownikService) {
        this.uzytkownikService = uzytkownikService;
    }

    @GetMapping
    public List<UzytkownikResponse> findAll(
            @RequestParam(required = false) Integer rolaId,
            @RequestParam(required = false) Boolean aktywny
    ) {
        return uzytkownikService.findAll(rolaId, aktywny);
    }

    @GetMapping("/{id}")
    public UzytkownikResponse findById(@PathVariable Integer id) {
        return uzytkownikService.findById(id);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return uzytkownikService.login(request);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UzytkownikResponse create(@Valid @RequestBody CreateUzytkownikRequest request) {
        return uzytkownikService.create(request);
    }

    @PutMapping("/{id}")
    public UzytkownikResponse update(@PathVariable Integer id, @Valid @RequestBody UpdateUzytkownikRequest request) {
        return uzytkownikService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public UzytkownikResponse updateStatus(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateStatusUzytkownikaRequest request
    ) {
        return uzytkownikService.updateStatus(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        uzytkownikService.delete(id);
    }
}
