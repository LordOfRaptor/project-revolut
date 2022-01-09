package fr.miage.revolut.services.validator;

import fr.miage.revolut.dto.patch.PatchAccount;
import org.springframework.stereotype.Service;

import javax.validation.Validator;

@Service
public class PacthAccountValidator extends  OurValidator<PatchAccount>{

    public PacthAccountValidator(Validator validator) {
        super(validator);
    }
}
