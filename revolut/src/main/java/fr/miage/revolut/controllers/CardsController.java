package fr.miage.revolut.controllers;


import fr.miage.revolut.config.annotations.IsUser;
import fr.miage.revolut.dto.create.NewCard;
import fr.miage.revolut.dto.view.CardView;
import fr.miage.revolut.entities.Card;
import fr.miage.revolut.services.CardsService;
import fr.miage.revolut.services.assembler.CardAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/accounts/{uuid}/cards", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@ExposesResourceFor(Card.class)
public class CardsController {

    private final CardsService cardsService;
    private final CardAssembler cardAssembler;

    @GetMapping
    @IsUser
    public ResponseEntity<CollectionModel<EntityModel<CardView>>> getCards(@PathVariable("uuid") String uuid){
        return ResponseEntity.ok(cardAssembler.toCollectionModelWithAccount(cardsService.findAllByAccount(uuid), uuid));

    }

    @GetMapping(value = "/{cardNumber}")
    @IsUser
    public ResponseEntity<EntityModel<CardView>> getCard(@PathVariable("uuid") String uuid,@PathVariable("cardNumber") String cardNumber){
        var a = cardsService.findCard(cardNumber,uuid);
        return Optional.ofNullable(a).filter(Optional::isPresent)
                .map(account -> ResponseEntity.ok(cardAssembler.toModelWithAccount(account.get(),uuid)))
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping
    @IsUser
    public ResponseEntity<?> createCard(@PathVariable("uuid") String uuid,@Valid @RequestBody NewCard card){
        var a = cardsService.create(card,uuid);
        URI location = linkTo(CardsController.class,uuid).slash(a.getCardNumber()).toUri();
        return ResponseEntity.created(location).build();

    }

    @DeleteMapping(value = "/{cardNumber}")
    @IsUser
    public ResponseEntity<?> deleteCard(@PathVariable("uuid") String uuid,@PathVariable("cardNumber") String cardNumber){
        var a = cardsService.deleteCard(cardNumber,uuid);
        return Optional.ofNullable(a).filter(Optional::isPresent)
                .map(card -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());

    }

    @PatchMapping(path = "/{cardNumber}")
    @IsUser
    public ResponseEntity<EntityModel<CardView>> patchCard(@PathVariable("uuid") String uuid, @PathVariable("cardNumber") String cardNumber, @RequestBody Map<Object, Object> fields) {
        var a = cardsService.patchAccount(cardNumber,uuid,fields);
        return Optional.ofNullable(a).filter(Optional::isPresent)
                .map(card -> ResponseEntity.ok(cardAssembler.toModelWithAccount(card.get(),uuid)))
                .orElse(ResponseEntity.notFound().build());


    }

}
