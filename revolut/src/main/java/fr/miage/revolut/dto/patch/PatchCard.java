package fr.miage.revolut.dto.patch;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class PatchCard {

    @NotNull
    private Boolean blocked =false;
    @NotNull
    private Boolean contactless=true;

    @Min(value = 0,message = "Le plafond ne peut être plus petit que {value}")
    @Max(value = 10000,message = "Le plafond ne peut être plus grand que {value}")
    private Integer limit;
    @NotNull
    private Boolean location=false;
}
