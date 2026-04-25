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
        Uzytkownik uzytkownik = repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> {
                    System.out.println("!!!! NIE ZNALEZIONO W BAZIE: " + email);
                    return new UsernameNotFoundException("Nie znaleziono: " + email);
                });

        return User.builder()
                .username(uzytkownik.getEmail())
                .password("{noop}" + uzytkownik.getHasloHash())
                .roles("USER")
                .build();
    }
}