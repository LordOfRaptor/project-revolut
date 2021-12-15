package fr.miage.revolut.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Getter
@Table(name = "ACCOUNT", schema = "revolut")
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "country")
    private String country;

    @Column(name = "passport", length = 9, unique = true)
    private String passport;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "iban", unique = true)
    private String iban;

    @Column(name = "token")
    private String token;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(uuid, account.uuid) && Objects.equals(name, account.name) && Objects.equals(surname, account.surname) && Objects.equals(country, account.country) && Objects.equals(passport, account.passport) && Objects.equals(phoneNumber, account.phoneNumber) && Objects.equals(iban, account.iban) && Objects.equals(token, account.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, surname, country, passport, phoneNumber, iban, token);
    }
}
