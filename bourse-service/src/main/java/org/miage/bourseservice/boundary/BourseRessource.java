package org.miage.bourseservice.boundary;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@RestController
public class BourseRessource {

    private TauxChangeRepository tcr;

    public BourseRessource(TauxChangeRepository tcr) {
        this.tcr = tcr;
    }

    @PostMapping(value = "/conversion/{source}/{destination}")
    public ResponseEntity<?> getValeurDeChange(@PathVariable(value = "source") String source, @PathVariable(value = "destination") String destination) {
        BigDecimal bdSource = tcr.findByPays(source.replaceAll("%20"," ")).getTauxConversion();
        BigDecimal bdDestination = tcr.findByPays(destination.replaceAll("%20"," ")).getTauxConversion();
        return ResponseEntity.ok(bdDestination.divide(bdSource, new MathContext(2,RoundingMode.HALF_DOWN)));
    }
}
