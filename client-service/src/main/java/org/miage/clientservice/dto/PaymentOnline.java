package org.miage.clientservice.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class PaymentOnline {

    private String cardNumber;
    private String cvv;
    private String country;
    private BigDecimal amount;
    private String creditAccount;
}
