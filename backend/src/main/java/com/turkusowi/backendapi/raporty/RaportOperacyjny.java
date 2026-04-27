package com.turkusowi.backendapi.raporty;

import com.turkusowi.backendapi.uzytkownicy.Uzytkownik;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "raporty_operacyjne")
public class RaportOperacyjny {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_raportu", nullable = false)
    private LocalDate dataRaportu;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sekretarz_id", nullable = false)
    private Uzytkownik sekretarz;

    @Column(name = "typ_raportu", nullable = false, length = 100)
    private String typRaportu;

    @Column(name = "uwagi", columnDefinition = "TEXT")
    private String uwagi;

    public Integer getId() {
        return id;
    }

    public LocalDate getDataRaportu() {
        return dataRaportu;
    }

    public void setDataRaportu(LocalDate dataRaportu) {
        this.dataRaportu = dataRaportu;
    }

    public Uzytkownik getSekretarz() {
        return sekretarz;
    }

    public void setSekretarz(Uzytkownik sekretarz) {
        this.sekretarz = sekretarz;
    }

    public String getTypRaportu() {
        return typRaportu;
    }

    public void setTypRaportu(String typRaportu) {
        this.typRaportu = typRaportu;
    }

    public String getUwagi() {
        return uwagi;
    }

    public void setUwagi(String uwagi) {
        this.uwagi = uwagi;
    }
}