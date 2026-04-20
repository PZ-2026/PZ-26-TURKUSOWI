package com.turkusowi.backendapi.uzytkownicy;

import com.turkusowi.backendapi.role.Rola;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "uzytkownicy")
public class Uzytkownik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "haslo_hash", nullable = false, length = 255)
    private String hasloHash;

    @Column(name = "imie", nullable = false, length = 100)
    private String imie;

    @Column(name = "nazwisko", nullable = false, length = 100)
    private String nazwisko;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rola_id", nullable = false)
    private Rola rola;

    @Column(name = "czy_aktywny", nullable = false)
    private boolean czyAktywny = true;

    @Column(name = "ostatnie_logowanie")
    private LocalDateTime ostatnieLogowanie;

    @Column(name = "data_utworzenia", nullable = false)
    private LocalDateTime dataUtworzenia;

    @Column(name = "data_modyfikacji", nullable = false)
    private LocalDateTime dataModyfikacji;

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        dataUtworzenia = now;
        dataModyfikacji = now;
    }

    @PreUpdate
    void preUpdate() {
        dataModyfikacji = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHasloHash() {
        return hasloHash;
    }

    public void setHasloHash(String hasloHash) {
        this.hasloHash = hasloHash;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public Rola getRola() {
        return rola;
    }

    public void setRola(Rola rola) {
        this.rola = rola;
    }

    public boolean isCzyAktywny() {
        return czyAktywny;
    }

    public void setCzyAktywny(boolean czyAktywny) {
        this.czyAktywny = czyAktywny;
    }

    public LocalDateTime getOstatnieLogowanie() {
        return ostatnieLogowanie;
    }

    public void setOstatnieLogowanie(LocalDateTime ostatnieLogowanie) {
        this.ostatnieLogowanie = ostatnieLogowanie;
    }

    public LocalDateTime getDataUtworzenia() {
        return dataUtworzenia;
    }

    public LocalDateTime getDataModyfikacji() {
        return dataModyfikacji;
    }
}
