package fr.miage.revolut.services;

import fr.miage.revolut.dto.create.NewCard;
import fr.miage.revolut.dto.patch.PatchAccount;
import fr.miage.revolut.dto.patch.PatchCard;
import fr.miage.revolut.entities.Account;
import fr.miage.revolut.entities.Card;
import fr.miage.revolut.mapper.CardsMapper;
import fr.miage.revolut.repositories.AccountsRepository;
import fr.miage.revolut.repositories.CardsRepository;
import fr.miage.revolut.services.validator.PatchCardValidator;
import fr.miage.revolut.util.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@ExposesResourceFor(Card.class)
public class CardsService {

    private final CardsRepository cardsRepository;
    private final CardsMapper cardsMapper;
    private final AccountsRepository accountsRepository;
    private final PatchCardValidator validator;
    private final RandomGenerator rand;


    public List<Card> findAllByAccount(String uuid){
        return cardsRepository.findByAccount_UuidAndDeleteFalse(uuid);
    }

    public Optional<Card> findCard(String cardNumber){
        return cardsRepository.findByCardNumberAndDeleteIsFalse(cardNumber);

    }

    public Card create(NewCard newCard,String uuid) {
        var card = cardsMapper.toEntity(newCard);
        card.setCardNumber(rand.generateNumericString(16));
        accountsRepository.findById(uuid).ifPresent(card::setAccount);
        card.setCode(rand.generateNumericString(4));
        card.setCvv(rand.generateNumericString(3));
        card.setDelete(false);
        card = cardsRepository.save(card);
        return card;
    }

    public Optional<Card> patchAccount(String cardNumber, Map<Object, Object> fields) {
        var cardOpt = cardsRepository.findByCardNumberAndDeleteIsFalse(cardNumber);
        if (cardOpt.isPresent()) {
            Card card = cardOpt.get();

            fields.forEach((f, v) -> {
                switch (f.toString()){
                    case "limit" -> card.setLimit((v!=null) ? Integer.parseInt(v.toString()) :  null);
                    case "blocked" -> card.setBlocked((v!=null) ? Boolean.valueOf(v.toString()):  null);
                    case "contacless" -> card.setContactless((v!=null) ? Boolean.valueOf(v.toString()):  null);
                    case "location" -> card.setLocation((v!=null) ? Boolean.valueOf(v.toString()):  null);
                }
            });
            PatchCard pc = new PatchCard();
            pc.setBlocked(card.getBlocked());
            pc.setContactless(card.getContactless());
            pc.setLimit(card.getLimit());
            pc.setLocation(card.getLocation());
            validator.validate(pc);
            cardsRepository.save(card);
            return Optional.ofNullable(card);
        }
        return Optional.empty();
    }

    public Optional<Card> deleteCard(String cardNumber){
        var cardOpt = cardsRepository.findByCardNumberAndDeleteIsFalse(cardNumber);
        if (cardOpt.isPresent()) {
            Card card = cardOpt.get();
            card.setDelete(true);
            cardsRepository.save(card);
            return Optional.ofNullable(card);
        }
        return Optional.empty();

    }


}
