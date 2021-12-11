package fr.miage.revolut.dto.create;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class NewTransaction {

    private BigDecimal amount;

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
