package fr.miage.revolut.services.validator;

import fr.miage.revolut.dto.create.NewAccount;
import org.springframework.stereotype.Service;

import javax.validation.Validator;

@Service
public class AccountsValidator extends OurValidator<NewAccount>{

    AccountsValidator(Validator validator) {
        super(validator);
    }
}
