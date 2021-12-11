package fr.miage.revolut.dto.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
public class CardView {

    @Size(min = 16,max = 16)
    private String cardNumber;

    private Boolean blocked;

    private Boolean virtual;

    private Boolean contactless;

    private Integer limit;

    private Boolean location;
}
