package org.miage.bourseservice.boundary;

import org.miage.bourseservice.entity.TauxChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TauxChangeRepository extends JpaRepository<TauxChange, Long> {
    TauxChange findBySourceAndCible(String source, String cible);
}
