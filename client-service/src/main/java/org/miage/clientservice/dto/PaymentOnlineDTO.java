package org.miage.clientservice.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class PaymentOnlineDTO {

    @Size(min = 16,max = 16)
    private String cardNumber;
    @Size(min = 3,max = 3)
    private String cvv;
    @NotBlank
    private String country;
    @Positive
    @Digits(integer=10, fraction=2)
    private BigDecimal amount;
}
