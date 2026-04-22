package com.turkusowi.backendapi.rezerwacje;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RezerwacjaSpaceruRepository extends JpaRepository<RezerwacjaSpaceru, Integer> {

    List<RezerwacjaSpaceru> findByZwierzeIdAndDataSpaceru(Integer zwierzeId, LocalDate dataSpaceru);

    boolean existsByZwierzeIdAndDataSpaceruAndGodzinaStartLessThanAndGodzinaKoniecGreaterThan(
            Integer zwierzeId,
            LocalDate dataSpaceru,
            LocalTime godzinaKoniec,
            LocalTime godzinaStart
    );
}
