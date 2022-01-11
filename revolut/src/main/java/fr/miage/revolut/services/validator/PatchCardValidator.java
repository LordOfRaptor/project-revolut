package fr.miage.revolut.services.validator;

import fr.miage.revolut.dto.patch.PatchCard;
import org.springframework.stereotype.Service;

import javax.validation.Validator;

@Service
public class PatchCardValidator extends OurValidator<PatchCard> {


    public PatchCardValidator(Validator validator) {
        super(validator);
    }
}
