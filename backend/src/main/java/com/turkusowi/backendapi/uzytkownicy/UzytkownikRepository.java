package com.turkusowi.backendapi.uzytkownicy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UzytkownikRepository extends JpaRepository<Uzytkownik, Integer> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<Uzytkownik> findByEmailIgnoreCase(String email);
}
