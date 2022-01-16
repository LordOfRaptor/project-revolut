package org.miage.bourseservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class TauxChange {

    @Id
    private String uuid;
    @Column(name = "pays",unique = true)
    private String pays;
    @Column(name = "code",unique = true)
    private String code;
    @Column(name = "taux_conversion")
    private BigDecimal tauxConversion;

    public TauxChange() {
        // JPA
    }

    public TauxChange(String uuid, String pays, String code, BigDecimal taux) {
        super();
        this.uuid = uuid;
        this.pays = pays;
        this.code = code;
        this.tauxConversion = taux;
    }

    public String getUuid() {
        return uuid;
    }

    public String getCode() {
        return code;
    }

    public String getPays() {
        return pays;
    }

    public BigDecimal getTauxConversion() {
        return tauxConversion;
    }


}
