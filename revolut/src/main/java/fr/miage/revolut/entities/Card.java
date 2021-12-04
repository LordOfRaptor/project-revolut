package fr.miage.revolut.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "CARD",schema = "revolut")
@Entity
@NoArgsConstructor
public class Card {

    @Id
    private String uuid;

    @Column(name = "card_number", length = 16)
    private String cardNumber;

    @Column(name = "code", length = 4)
    private String code;

    @Column(name = "cvv", length = 3)
    private String cvv;

    @Column(name = "blocked")
    private Boolean blocked;

    @Column(name = "virtual")
    private Boolean virtual;

    @Column(name = "contactless")
    private Boolean contactless;

    @Column(name = "limit")
    private Integer limit;

    @Column(name = "location")
    private Boolean location;

/**

     * • localisation (oui/non)
     */

}
