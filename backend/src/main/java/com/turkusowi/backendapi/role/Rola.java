package com.turkusowi.backendapi.role;

import jakarta.persistence.*;

@Entity
@Table(name = "\"role\"")
public class Rola {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nazwa", nullable = false, unique = true, length = 100)
    private String nazwa;

    public Integer getId() {
        return id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }
}
