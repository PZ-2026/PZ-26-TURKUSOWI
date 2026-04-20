package com.turkusowi.backendapi.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolaRepository extends JpaRepository<Rola, Integer> {

    Optional<Rola> findByNazwaIgnoreCase(String nazwa);
}
