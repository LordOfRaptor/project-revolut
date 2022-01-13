package fr.miage.revolut.repositories;

import fr.miage.revolut.entities.Account;
import fr.miage.revolut.entities.Card;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("nosecurity")
public class CardRepositoryUnitTest {

    private static Account a1,a2;
    private static Card c1,c2,c3;

    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @BeforeAll
    static void init() {
        a1 = new Account();
        a1.setUuid("1234");
        a2 = new Account();
        a2.setUuid("4321");
        c1 = new Card();
        c1.setCardNumber("1234567891234567");
        c1.setAccount(a1);
        c1.setDelete(false);
        c2 = new Card();
        c2.setCardNumber("2234567891234567");
        c2.setAccount(a1);
        c2.setDelete(false);
        c3 = new Card();
        c3.setCardNumber("3334567891234567");
        c3.setAccount(a2);
        c3.setDelete(false);
    }

    @BeforeEach
    void beforeEachInit(){
        accountsRepository.save(a1);
        accountsRepository.save(a2);
        cardsRepository.save(c1);
        cardsRepository.save(c2);
        cardsRepository.save(c3);
    }

    @AfterEach
    void unset() {
        cardsRepository.deleteAll();
        accountsRepository.deleteAll();

    }

    @Test
    void findCardSucces(){
        var card = cardsRepository.findByCardNumberAndAccount_UuidAndDeleteIsFalse(c1.getCardNumber(), a1.getUuid());
        assertTrue(card.isPresent());
    }

    @Test
    void findCardFailure(){
        var card = cardsRepository.findByCardNumberAndAccount_UuidAndDeleteIsFalse(c3.getCardNumber(), a1.getUuid());
        assertFalse(card.isPresent());
    }

    @Test
    void findCardsSucces(){
        var cards = cardsRepository.findByAccount_UuidAndDeleteFalse(a1.getUuid());
        assertEquals(2,cards.size());
    }

    @Test
    void findCardsSuccesWithOneDelete(){
        Card c4 = new Card();
        c4.setCardNumber("4444567891234567");
        c4.setAccount(a1);
        c4.setDelete(true);
        cardsRepository.save(c4);
        var cards = cardsRepository.findByAccount_UuidAndDeleteFalse(a1.getUuid());
        assertEquals(2,cards.size());
    }

    @Test
    void existingCardNumber(){
        assertFalse(cardsRepository.existsByCardNumber("5555567891234567"));
    }

    @Test
    void noneExistingCardNumber(){
        assertTrue(cardsRepository.existsByCardNumber("1234567891234567"));
    }



}
