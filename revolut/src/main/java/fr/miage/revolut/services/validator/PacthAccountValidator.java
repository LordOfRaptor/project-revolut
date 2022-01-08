package fr.miage.revolut.services.validator;

import fr.miage.revolut.dto.patch.PatchAccount;

import javax.validation.Validator;

public class PacthAccountValidator extends  OurValidator<PatchAccount>{

    public PacthAccountValidator(Validator validator) {
        super(validator);
    }
}
