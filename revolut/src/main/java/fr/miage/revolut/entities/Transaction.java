package fr.miage.revolut.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Table(name = "TRANSACTION",schema = "revolut")
@Entity
@NoArgsConstructor
public class Transaction {

    @Id
    private String uuid;

    @Column(name = "date")
    private OffsetDateTime date;

    @Column(name = "amount")
    private long amount;

    @Column(name = "change_rate", precision = 10,scale = 2)
    private BigDecimal changeRate;

    @Column(name = "category")
    private String category;

    @Column(name = "country")
    private String country;

    //Receive
    @Column(name = "credit_account")
    private String creditAccount;

    @Column(name = "credit_account_name")
    private String creditcAcountName;

    //Send
    @Column(name = "debtor_account")
    private String debtorAccount;

    @Column(name = "debtor_account_name")
    private String debtorcAcountName;

    @Column(name = "label")
    private String label;

}
