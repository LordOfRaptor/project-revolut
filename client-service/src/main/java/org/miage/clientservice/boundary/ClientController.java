package org.miage.clientservice.boundary;

import lombok.RequiredArgsConstructor;
import org.miage.clientservice.dto.PaymentOnline;
import org.miage.clientservice.dto.PaymentOnlineDTO;
import org.miage.clientservice.dto.PaymentPhysique;
import org.miage.clientservice.dto.PaymentPhysiqueDTO;
import org.miage.clientservice.feign.FeignRevolut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ClientController {

    private final FeignRevolut revolut;
    @Value("${app.iban}")
    private String iban;

    @PostMapping(value = "/paymentPhysique")
    public ResponseEntity<?> createCard(@RequestBody @Valid PaymentPhysiqueDTO paymentPhysique){
        PaymentPhysique payment = new PaymentPhysique();
        payment.setCardNumber(paymentPhysique.getCardNumber());
        payment.setCode(paymentPhysique.getCode());
        payment.setCreditAccount(iban);
        payment.setContacless(paymentPhysique.getContacless());
        payment.setAmount(paymentPhysique.getAmount());
        payment.setCountry(paymentPhysique.getCountry());
        return revolut.postPaiement(payment);

    }

    @PostMapping(value = "/paymentOnline")
    public ResponseEntity<?> createCard(@RequestBody @Valid PaymentOnlineDTO paymentOnline){
        PaymentOnline payment = new PaymentOnline();
        payment.setCardNumber(paymentOnline.getCardNumber());
        payment.setCvv(paymentOnline.getCvv());
        payment.setCreditAccount(iban);
        payment.setAmount(paymentOnline.getAmount());
        payment.setCountry(paymentOnline.getCountry());
        return revolut.postPaiement(payment);

    }
}
