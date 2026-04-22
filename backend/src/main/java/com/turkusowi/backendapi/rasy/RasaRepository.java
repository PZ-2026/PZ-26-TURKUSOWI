package com.turkusowi.backendapi.rasy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RasaRepository extends JpaRepository<Rasa, Integer> {

    Optional<Rasa> findByRasaIgnoreCase(String rasa);
}
