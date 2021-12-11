package fr.miage.revolut.dto.create;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewCard {

    private Boolean blocked;

    private Boolean virtual;

    private Boolean contactless;

    private Integer limit;

    private Boolean location;
}
