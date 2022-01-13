package fr.miage.revolut.services.validator;

import fr.miage.revolut.dto.patch.PatchTransaction;
import org.springframework.stereotype.Service;

import javax.validation.Validator;

@Service
public class PatchTransactionValidator extends OurValidator<PatchTransaction>{

    public PatchTransactionValidator(Validator validator) {
        super(validator);
    }
}
