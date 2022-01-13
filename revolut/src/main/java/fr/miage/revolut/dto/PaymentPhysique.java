package fr.miage.revolut.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class PaymentPhysique {

    @Size(min = 16,max = 16)
    private String cardNumber;
    private String code;
    @NotBlank
    private String country;
    @NotNull
    private Boolean contacless;
    @NotBlank
    private String creditAccount;
    @Positive
    @Digits(integer=10, fraction=2)
    private BigDecimal amount;
}
