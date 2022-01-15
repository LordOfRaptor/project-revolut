package fr.miage.revolut.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

//@Getter
//@Setter
//@Table(name = "PIVOT_CARD_TRANSACTION", schema = "revolut")
//@Entity
@Setter
@Getter
@Embeddable
@NoArgsConstructor
public class TransactionCard implements Serializable {

    //@Id
    @OneToOne
    @JoinColumn(name = "transaction_uuid",referencedColumnName = "uuid",unique = true)
    private Transaction transaction;

    //@Id
    @ManyToOne
    @JoinColumn(name = "card_number",referencedColumnName = "card_number")
    private Card card;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionCard that = (TransactionCard) o;
        return Objects.equals(transaction, that.transaction) && Objects.equals(card, that.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaction, card);
    }
}


