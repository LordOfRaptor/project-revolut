package fr.miage.revolut.dto.create;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewCard {

    private Boolean blocked;

    private Boolean virtual;

    private Boolean contactless;

    private Integer limit;

    private Boolean location;
}
