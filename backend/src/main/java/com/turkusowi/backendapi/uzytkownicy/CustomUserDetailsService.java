package com.turkusowi.backendapi.uzytkownicy;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UzytkownikRepository repository;

    public CustomUserDetailsService(UzytkownikRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("!!!! Proba logowania dla email: " + email); // DODAJ TO

        Uzytkownik uzytkownik = repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> {
                    System.out.println("!!!! NIE ZNALEZIONO W BAZIE: " + email);
                    return new UsernameNotFoundException("Nie znaleziono: " + email);
                });

        System.out.println("!!!! ZNALEZIONO W BAZIE. Haslo to: " + uzytkownik.getHasloHash());

        return User.builder()
                .username(uzytkownik.getEmail())
                .password("{noop}" + uzytkownik.getHasloHash()) // Sprawdź czy masz {noop}!
                .roles("USER")
                .build();
    }
}