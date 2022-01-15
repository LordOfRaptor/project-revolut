package org.miage.bourseservice.boundary;

import org.miage.bourseservice.entity.TauxChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TauxChangeRepository extends JpaRepository<TauxChange, String> {
    TauxChange findByPays(String pays);


}
