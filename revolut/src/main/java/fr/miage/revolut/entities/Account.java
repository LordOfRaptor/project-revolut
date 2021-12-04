package fr.miage.revolut.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "ACCOUNT",schema = "revolut")
@Setter
@NoArgsConstructor
public class Account{

    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "country")
    private String country;

    @Column(name = "passport", length = 9)
    private String passport;

    @Column(name = "phone_number")
    //@Pattern(regexp = "^\\+[1-9]{1,14}$")
    private String phoneNumber;

    @Column(name = "iban",unique = true)
    private String iban;

    @Column(name = "token")
    private String token;

    @Column(name = "password")
    private String password;

}
