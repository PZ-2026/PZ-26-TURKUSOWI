package com.turkusowi.backendapi.role;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RolaController {

    private final RolaService rolaService;

    public RolaController(RolaService rolaService) {
        this.rolaService = rolaService;
    }

    @GetMapping
    public List<RolaResponse> findAll() {
        return rolaService.findAll();
    }
}
