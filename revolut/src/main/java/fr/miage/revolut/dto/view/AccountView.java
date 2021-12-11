package fr.miage.revolut.dto.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class AccountView {

    @Size(min = 20,max = 27)
    private String iban;

    private String name;

    private String surname;

    private String country;

    @Size(min =9,max = 9)
    private String passport;

    @NotEmpty
    @Pattern(regexp = "^\\+[1-9]{1,14}$")
    private String phoneNumber;
}
