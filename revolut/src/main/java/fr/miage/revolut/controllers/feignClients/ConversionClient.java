package fr.miage.revolut.controllers.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;

@FeignClient(value = "conversion-service",url = "${app.conversion-service}")
public interface ConversionClient {

    @RequestMapping(method = RequestMethod.POST, value = "/conversion/{source}/{destination}", consumes = "application/json")
    BigDecimal getConversion(@PathVariable("source") String source, @PathVariable("destination") String destination);


}
