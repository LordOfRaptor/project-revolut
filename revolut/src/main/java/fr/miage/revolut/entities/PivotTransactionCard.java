package fr.miage.revolut.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "PIVOT_CARD_TRANSACTION", schema = "revolut")
@Entity
@NoArgsConstructor
public class PivotTransactionCard {

    @EmbeddedId
    private TransactionCard transactionCard;

}
