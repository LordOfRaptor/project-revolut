package fr.miage.revolut.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Table(name = "CARD", schema = "revolut")
@Entity
@NoArgsConstructor
public class Card {


    @Id
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

    @Column(name = "`limit`")
    private Integer limit;

    @Column(name = "location")
    private Boolean location;

    @Column(name = "delete")
    private Boolean delete;

    @ManyToOne
    @JoinColumn(name = "account_uuid",referencedColumnName = "uuid")
    private Account account;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(cardNumber, card.cardNumber) && Objects.equals(code, card.code) && Objects.equals(cvv, card.cvv) && Objects.equals(blocked, card.blocked) && Objects.equals(virtual, card.virtual) && Objects.equals(contactless, card.contactless) && Objects.equals(limit, card.limit) && Objects.equals(location, card.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, code, cvv, blocked, virtual, contactless, limit, location);
    }
}
