package org.miage.clientservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class PaymentPhysique {


    private String creditAccount;
    private String cardNumber;
    private String code;
    private String country;
    private Boolean contacless;
    private BigDecimal amount;
}
