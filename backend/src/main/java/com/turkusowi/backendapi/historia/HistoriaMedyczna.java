package com.turkusowi.backendapi.historia;

import com.turkusowi.backendapi.uzytkownicy.Uzytkownik;
import com.turkusowi.backendapi.zwierzeta.Zwierze;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historia_medyczna")
public class HistoriaMedyczna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zwierze_id", nullable = false)
    private Zwierze zwierze;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pracownik_id", nullable = false)
    private Uzytkownik pracownik;

    @Column(name = "opis_zabiegu", nullable = false)
    private String opisZabiegu;

    @Column(name = "data_wpisu", nullable = false)
    private LocalDateTime dataWpisu;

    @PrePersist
    void prePersist() {
        if (dataWpisu == null) {
            dataWpisu = LocalDateTime.now();
        }
    }

    public Integer getId() {
        return id;
    }

    public Zwierze getZwierze() {
        return zwierze;
    }

    public void setZwierze(Zwierze zwierze) {
        this.zwierze = zwierze;
    }

    public Uzytkownik getPracownik() {
        return pracownik;
    }

    public void setPracownik(Uzytkownik pracownik) {
        this.pracownik = pracownik;
    }

    public String getOpisZabiegu() {
        return opisZabiegu;
    }

    public void setOpisZabiegu(String opisZabiegu) {
        this.opisZabiegu = opisZabiegu;
    }

    public LocalDateTime getDataWpisu() {
        return dataWpisu;
    }

    public void setDataWpisu(LocalDateTime dataWpisu) {
        this.dataWpisu = dataWpisu;
    }
}
