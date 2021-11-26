package fr.miage.revolut.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Account{

    @Id
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

    @Column(name = "iban")
    private String iban;

//@Column(name = "token")
    //private String token;



}
