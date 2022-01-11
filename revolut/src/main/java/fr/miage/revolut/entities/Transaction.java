package fr.miage.revolut.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
@Setter
@Table(name = "TRANSACTION", schema = "revolut")
@Entity
@NoArgsConstructor
public class Transaction {

    @Id
    private String uuid;

    @Column(name = "date")
    private OffsetDateTime date;

    @Column(name = "amount")
    private long amount;

    @Column(name = "change_rate", precision = 10, scale = 2)
    private BigDecimal changeRate;

    @Column(name = "category")
    private String category;

    @Column(name = "country")
    private String country;

    //Receive
    @Column(name = "credit_account")
    private String creditAccount;

    @Column(name = "credit_account_name")
    private String creditAcountName;

    //Send
    @Column(name = "debtor_account")
    private String debtorAccount;

    @Column(name = "debtor_account_name")
    private String debtorAcountName;

    @Column(name = "label")
    private String label;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return amount == that.amount && Objects.equals(uuid, that.uuid) && Objects.equals(date, that.date) && Objects.equals(changeRate, that.changeRate) && Objects.equals(category, that.category) && Objects.equals(country, that.country) && Objects.equals(creditAccount, that.creditAccount) && Objects.equals(creditAcountName, that.creditAcountName) && Objects.equals(debtorAccount, that.debtorAccount) && Objects.equals(debtorAcountName, that.debtorAcountName) && Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, date, amount, changeRate, category, country, creditAccount, creditAcountName, debtorAccount, debtorAcountName, label);
    }
}
