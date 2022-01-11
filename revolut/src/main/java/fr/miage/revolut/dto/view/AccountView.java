package fr.miage.revolut.dto.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountView {

    private String iban;

    private String name;

    private String surname;

    private String country;

    private String passport;

    private String phoneNumber;

    private String solde;
}
