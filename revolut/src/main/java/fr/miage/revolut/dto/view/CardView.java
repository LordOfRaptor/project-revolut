package fr.miage.revolut.dto.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CardView {

    private String cardNumber;

    private String code;

    private String cvv;

    private Boolean blocked;

    private Boolean virtual;

    private Boolean contactless;

    private Integer limit;

    private Boolean location;
}
