package fr.miage.revolut.dto.create;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class NewAccount {

    @NotBlank(message = "name invalid")
    @Size(min = 2, message = "name invalid")
    private String name;

    @NotBlank(message = "surname invalid")
    @Size(min = 2, message = "surname invalid")
    private String surname;

    @NotBlank(message = "country invalid")
    //Shortest country name is 4
    @Size(min = 4, message = "country invalid")
    private String country;

    @Size(min = 9, max = 9)
    @Pattern(regexp = "^[0-9A-Z]{9}$", message = "Numero de passport invalide")
    private String passport;

    @NotEmpty
    @Pattern(regexp = "^\\+[1-9][0-9]{7,13}$", message = "Numero de telephone invalide")
    private String phoneNumber;
}
