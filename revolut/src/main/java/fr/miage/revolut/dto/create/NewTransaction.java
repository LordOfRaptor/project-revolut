package fr.miage.revolut.dto.create;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewTransaction {

    private long amount;

    private String category;

    private String country;

    private String label;

    //receive
    private String creditAccount;
    private String creditAcountName;

    //Send
    private String debtorAccount;
    private String debtorAcountName;
}
