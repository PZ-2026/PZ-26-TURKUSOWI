package com.turkusowi.backendapi.uzytkownicy;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UzytkownikRepository extends JpaRepository<Uzytkownik, Integer> {

    boolean existsByEmailIgnoreCase(String email);
}
