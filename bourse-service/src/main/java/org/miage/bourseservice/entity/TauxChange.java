package org.miage.bourseservice.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TauxChange {

    @Id
    private String uuid;
    @Column(name = "devise_source")
    private String source;
    @Column(name = "devise_cible")
    private String cible;
    private BigDecimal tauxConversion;

    public TauxChange() {
        // JPA
    }

    public TauxChange(String uuid, String source, String cible, BigDecimal taux) {
        super();
        this.uuid = uuid;
        this.source = source;
        this.cible = cible;
        this.tauxConversion = taux;
    }

    public String getUuid() {
        return uuid;
    }

    public String getSource() {
        return source;
    }

    public String getCible() {
        return cible;
    }

    public BigDecimal getTauxConversion() {
        return tauxConversion;
    }


}
