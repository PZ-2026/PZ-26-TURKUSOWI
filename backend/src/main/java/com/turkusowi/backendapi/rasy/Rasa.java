package com.turkusowi.backendapi.rasy;

import jakarta.persistence.*;

@Entity
@Table(name = "rasy")
public class Rasa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "typ_zwierzecia", nullable = false, length = 100)
    private String typZwierzecia;

    @Column(name = "rasa", nullable = false, length = 100)
    private String rasa;

    public Rasa() {
    }

    public Rasa(String typZwierzecia, String rasa) {
        this.typZwierzecia = typZwierzecia;
        this.rasa = rasa;
    }

    public Integer getId() {
        return id;
    }

    public String getTypZwierzecia() {
        return typZwierzecia;
    }

    public void setTypZwierzecia(String typZwierzecia) {
        this.typZwierzecia = typZwierzecia;
    }

    public String getRasa() {
        return rasa;
    }

    public void setRasa(String rasa) {
        this.rasa = rasa;
    }
}