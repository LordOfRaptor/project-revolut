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

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String country;

    @Size(min =9,max = 9)
    private String passport;

    @NotEmpty
    @Pattern(regexp = "^\\+[1-9]{1,14}$")
    private String phoneNumber;
}
