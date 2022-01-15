package org.miage.clientservice.feign;

import org.miage.clientservice.dto.PaymentOnline;
import org.miage.clientservice.dto.PaymentOnlineDTO;
import org.miage.clientservice.dto.PaymentPhysique;
import org.miage.clientservice.dto.PaymentPhysiqueDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "revolut",url = "${app.revolut}")
public interface FeignRevolut {

    @RequestMapping(method = RequestMethod.POST, value = "/paiement/physique", consumes = "application/json")
    ResponseEntity<?> postPaiement(PaymentPhysique paymentPhysique);


    @RequestMapping(method = RequestMethod.POST, value = "/paiement/online", consumes = "application/json")
    ResponseEntity<?> postPaiement(PaymentOnline paymentOnline);
}
