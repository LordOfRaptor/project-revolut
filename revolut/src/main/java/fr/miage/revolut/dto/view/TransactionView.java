package fr.miage.revolut.dto.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransactionView {

    private long amount;

    private String category;

    private String country;

    private String label;

    private OffsetDateTime date;

    //receive
    private String creditAccount;
    private String creditAcountName;

    //Send
    private String debtorAccount;
    private String debtorAcountName;
}
