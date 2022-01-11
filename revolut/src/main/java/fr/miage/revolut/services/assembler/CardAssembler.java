package fr.miage.revolut.services.assembler;

import fr.miage.revolut.controllers.AccountsController;
import fr.miage.revolut.controllers.CardsController;
import fr.miage.revolut.dto.view.AccountView;
import fr.miage.revolut.dto.view.CardView;
import fr.miage.revolut.entities.Account;
import fr.miage.revolut.entities.Card;
import fr.miage.revolut.mapper.AccountsMapper;
import fr.miage.revolut.mapper.CardsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class CardAssembler implements RepresentationModelAssembler<Card, EntityModel<CardView>> {

    private final CardsMapper mapper;

    @Override
    public EntityModel<CardView> toModel(Card entity) {
        return EntityModel.of(mapper.toDto(entity));
    }

    public EntityModel<CardView> toModelWithAccount(Card entity,String uuid){
        return EntityModel.of(mapper.toDto(entity),
                linkTo(methodOn(CardsController.class)
                        .getCards(uuid)).withRel("collections"),
                linkTo(methodOn(CardsController.class)
                        .getCard(uuid, entity.getCardNumber())).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<CardView>> toCollectionModel(Iterable<? extends Card> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }

    public CollectionModel<EntityModel<CardView>> toCollectionModelWithAccount(Iterable<? extends Card> entities,String uuid) {
        List<EntityModel<CardView>> cardModel = StreamSupport
                .stream(entities.spliterator(), false)
                .map(i -> toModelWithAccount(i,uuid))
                .toList();
        return CollectionModel.of(cardModel,
                linkTo(methodOn(CardsController.class)
                        .getCards(uuid)).withSelfRel());
    }
}

