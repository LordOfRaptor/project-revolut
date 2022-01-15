package fr.miage.revolut.controllers;



import fr.miage.revolut.dto.PaymentOnline;
import fr.miage.revolut.dto.PaymentPhysique;
import fr.miage.revolut.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/paiement", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;



    @PostMapping(value = "/physique")
    public ResponseEntity<?> createTransactionPhysique(@RequestBody @Valid PaymentPhysique payment){
        var res = paymentService.createTransaction(payment);
        if(res.isEmpty())
            return ResponseEntity.status(403).build();
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/online")
    public ResponseEntity<?> createTransactionOnline(@RequestBody @Valid PaymentOnline payment){
        var res = paymentService.createTransaction(payment);
        if(res.isEmpty())
            return ResponseEntity.status(403).build();
        return ResponseEntity.noContent().build();


    }
}
