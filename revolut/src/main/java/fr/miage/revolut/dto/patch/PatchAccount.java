package fr.miage.revolut.dto.patch;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class PatchAccount {

    @Size(min = 9, max = 9, message = "Numero de passport invalide must respect a size of {min}")
    @Pattern(regexp = "^[0-9A-Z]{9}$", message = "Numero de passport invalide")
    private String passport;

    @NotEmpty
    @Pattern(regexp = "^\\+[1-9][0-9]{7,13}$", message = "Numero de telephone invalide")
    private String phoneNumber;

}
