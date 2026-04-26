package com.turkusowi.backendapi.rezerwacje;

import com.turkusowi.backendapi.uzytkownicy.Uzytkownik;
import com.turkusowi.backendapi.zwierzeta.Zwierze;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "rezerwacje_spacerow")
public class RezerwacjaSpaceru {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wolontariusz_id", nullable = false)
    private Uzytkownik wolontariusz;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zwierze_id", nullable = false)
    private Zwierze zwierze;

    @Column(name = "data_spaceru", nullable = false)
    private LocalDate dataSpaceru;

    @Column(name = "godzina_start", nullable = false)
    private LocalTime godzinaStart;

    @Column(name = "godzina_koniec", nullable = false)
    private LocalTime godzinaKoniec;

    @Column(name = "uwagi", columnDefinition = "TEXT")
    private String uwagi;

    @Column(name = "data_zapisu", nullable = false)
    private LocalDateTime dataZapisu;

    @PrePersist
    void prePersist() {
        if (dataZapisu == null) {
            dataZapisu = LocalDateTime.now();
        }
    }

    public Integer getId() {
        return id;
    }

    public Uzytkownik getWolontariusz() {
        return wolontariusz;
    }

    public void setWolontariusz(Uzytkownik wolontariusz) {
        this.wolontariusz = wolontariusz;
    }

    public Zwierze getZwierze() {
        return zwierze;
    }

    public void setZwierze(Zwierze zwierze) {
        this.zwierze = zwierze;
    }

    public LocalDate getDataSpaceru() {
        return dataSpaceru;
    }

    public void setDataSpaceru(LocalDate dataSpaceru) {
        this.dataSpaceru = dataSpaceru;
    }

    public LocalTime getGodzinaStart() {
        return godzinaStart;
    }

    public void setGodzinaStart(LocalTime godzinaStart) {
        this.godzinaStart = godzinaStart;
    }

    public LocalTime getGodzinaKoniec() {
        return godzinaKoniec;
    }

    public void setGodzinaKoniec(LocalTime godzinaKoniec) {
        this.godzinaKoniec = godzinaKoniec;
    }

    public String getUwagi() {
        return uwagi;
    }

    public void setUwagi(String uwagi) {
        this.uwagi = uwagi;
    }

    public LocalDateTime getDataZapisu() {
        return dataZapisu;
    }
}