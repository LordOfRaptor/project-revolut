package fr.miage.revolut.dto.create;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class NewTransaction {

    @Positive
    @Digits(integer=10, fraction=2)
    @NotNull
    private BigDecimal amount;
    @Size(max = 15, message = "category invalid size max is {max}")
    private String category;
    @Size(min = 4, message = "country invalid")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "country invalid")
    private String country;
    @Size(max = 25, message = "label invalid size max is {max}")
    private String label;

    //receive
    @NotBlank
    private String creditAccount;
    @Size(max = 25, message = "creditAccountName invalid size max is {max}")
    private String creditAccountName;

    @Size(max = 25, message = "debtorAccountName invalid size max is {max}")
    private String debtorAccountName;
}
