package org.miage.bourseservice.boundary;

import org.miage.bourseservice.entity.TauxChange;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BourseRessource {

    private Environment environment;
    private TauxChangeRepository tcr;

    public BourseRessource(Environment env, TauxChangeRepository tcr) {
        this.environment = env;
        this.tcr = tcr;
    }

    @GetMapping("/conversion/{source}/{destination}")
    public TauxChange getValeurDeChange(@PathVariable String source, @PathVariable String cible) {
        TauxChange tauxChange = tcr.findBySourceAndCible(source, cible);

        return tauxChange;
    }
}
