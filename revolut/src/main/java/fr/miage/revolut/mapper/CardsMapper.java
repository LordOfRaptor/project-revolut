package fr.miage.revolut.mapper;

import fr.miage.revolut.dto.create.NewCard;
import fr.miage.revolut.dto.view.CardView;
import fr.miage.revolut.entities.Card;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardsMapper {

    CardView toDto(Card card);

    Card toEntity(NewCard dto);
}
