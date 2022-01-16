package fr.miage.revolut.dto.patch;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class PatchTransaction {

    @Size(max = 15, message = "category invalid size max is {max}")
    private String category;
    @Size(max = 25, message = "label invalid size max is {max}")
    private String label;
    @Size(max = 25, message = "creditAccountName invalid size max is {max}")
    private String creditAccountName;
    @Size(max = 25, message = "debtorAccountName invalid size max is {max}")
    private String debtorAccountName;
}
