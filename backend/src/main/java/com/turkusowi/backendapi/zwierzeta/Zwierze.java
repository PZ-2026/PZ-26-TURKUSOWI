package com.turkusowi.backendapi.zwierzeta;

import com.turkusowi.backendapi.rasy.Rasa;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "zwierzeta")
public class Zwierze {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "imie", nullable = false, length = 100)
    private String imie;

    @Column(name = "wiek")
    private Integer wiek;

    @Column(name = "waga", precision = 10, scale = 2)
    private BigDecimal waga;

    @Column(name = "plec", length = 50)
    private String plec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rasa_id")
    private Rasa rasa;

    @Column(name = "status", length = 100)
    private String status;

    @Column(name = "opis", columnDefinition = "TEXT")
    private String opis;

    @Column(name = "charakter", columnDefinition = "TEXT")
    private String charakter;

    @Column(name = "preferencje_zywieniowe", columnDefinition = "TEXT")
    private String preferencjeZywieniowe;

    @Column(name = "data_modyfikacji", nullable = false)
    private LocalDateTime dataModyfikacji;

    @PrePersist
    @PreUpdate
    void updateModificationDate() {
        dataModyfikacji = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public Integer getWiek() {
        return wiek;
    }

    public void setWiek(Integer wiek) {
        this.wiek = wiek;
    }

    public BigDecimal getWaga() {
        return waga;
    }

    public void setWaga(BigDecimal waga) {
        this.waga = waga;
    }

    public String getPlec() {
        return plec;
    }

    public void setPlec(String plec) {
        this.plec = plec;
    }

    public Rasa getRasa() {
        return rasa;
    }

    public void setRasa(Rasa rasa) {
        this.rasa = rasa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getCharakter() {
        return charakter;
    }

    public void setCharakter(String charakter) {
        this.charakter = charakter;
    }

    public String getPreferencjeZywieniowe() {
        return preferencjeZywieniowe;
    }

    public void setPreferencjeZywieniowe(String preferencjeZywieniowe) {
        this.preferencjeZywieniowe = preferencjeZywieniowe;
    }

    public LocalDateTime getDataModyfikacji() {
        return dataModyfikacji;
    }
}