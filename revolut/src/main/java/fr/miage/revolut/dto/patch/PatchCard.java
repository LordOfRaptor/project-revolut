package fr.miage.revolut.dto.patch;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class PatchCard {


    private Boolean blocked;
    private Boolean contactless;

    @Min(value = 0,message = "Le plafond ne peut être plus petit que {value}")
    @Max(value = 10000,message = "Le plafond ne peut être plus grand que {value}")
    private Integer limit;
    private Boolean location;
}
