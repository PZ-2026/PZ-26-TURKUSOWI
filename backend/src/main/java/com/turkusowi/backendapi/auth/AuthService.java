package com.turkusowi.backendapi.auth;

import com.turkusowi.backendapi.common.ConflictException;
import com.turkusowi.backendapi.common.NotFoundException;
import com.turkusowi.backendapi.role.Rola;
import com.turkusowi.backendapi.role.RolaService;
import com.turkusowi.backendapi.uzytkownicy.Uzytkownik;
import com.turkusowi.backendapi.uzytkownicy.UzytkownikRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UzytkownikRepository uzytkownikRepository;
    private final RolaService rolaService;

    public AuthService(UzytkownikRepository uzytkownikRepository, RolaService rolaService) {
        this.uzytkownikRepository = uzytkownikRepository;
        this.rolaService = rolaService;
    }

    public AuthUserResponse login(LoginRequest request) {
        Uzytkownik uzytkownik = uzytkownikRepository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new NotFoundException("Nie znaleziono konta dla podanego adresu email."));

        if (!uzytkownik.isCzyAktywny()) {
            throw new ConflictException("To konto jest nieaktywne.");
        }

        if (!uzytkownik.getHasloHash().equals(request.password())) {
            throw new ConflictException("Niepoprawne dane logowania.");
        }

        uzytkownik.setOstatnieLogowanie(LocalDateTime.now());
        return mapToAuthUser(uzytkownikRepository.save(uzytkownik));
    }

    public AuthUserResponse register(RegisterRequest request) {
        String normalizedEmail = request.email().trim();
        if (uzytkownikRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new ConflictException("Uzytkownik z adresem email '" + normalizedEmail + "' juz istnieje.");
        }

        Rola volunteerRole = rolaService.getRequiredVolunteerRole();

        Uzytkownik uzytkownik = new Uzytkownik();
        uzytkownik.setImie(request.imie().trim());
        uzytkownik.setNazwisko(request.nazwisko().trim());
        uzytkownik.setEmail(normalizedEmail);
        uzytkownik.setHasloHash(request.password());
        uzytkownik.setRola(volunteerRole);
        uzytkownik.setCzyAktywny(true);

        return mapToAuthUser(uzytkownikRepository.save(uzytkownik));
    }

    public AuthMessageResponse forgotPassword(ForgotPasswordRequest request) {
        uzytkownikRepository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new NotFoundException("Nie znaleziono konta dla podanego adresu email."));

        return new AuthMessageResponse("Wyslalismy instrukcje resetu hasla na podany adres email.");
    }

    private AuthUserResponse mapToAuthUser(Uzytkownik uzytkownik) {
        return new AuthUserResponse(
                uzytkownik.getId(),
                uzytkownik.getEmail(),
                uzytkownik.getImie(),
                uzytkownik.getNazwisko(),
                uzytkownik.getRola().getNazwa(),
                uzytkownik.isCzyAktywny()
        );
    }
}
