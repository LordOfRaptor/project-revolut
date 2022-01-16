package fr.miage.revolut.repositories;

import fr.miage.revolut.entities.Card;
import fr.miage.revolut.entities.PivotTransactionCard;
import fr.miage.revolut.entities.Transaction;
import fr.miage.revolut.entities.TransactionCard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("nosecurity")
public class TransactionCardRepositoryUnitTest {

    private static Card c1,c2;
    private static Transaction t1,t2,t3;
    private static PivotTransactionCard pt1,pt2,pt3;
    private static OffsetDateTime odt;

    @Autowired
    private TransactionCardRepository transactionCardRepository;

    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @AfterEach
    void unset() {
        transactionCardRepository.deleteAll();
        cardsRepository.deleteAll();
        transactionsRepository.deleteAll();
    }

    @BeforeAll
    static void init(){
        odt = OffsetDateTime.now();
        c1 = new Card();
        c1.setCardNumber("1234567891234567");
        c2 = new Card();
        c2.setCardNumber("2234567891234567");
        t1 = new Transaction();
        t1.setAmount("500");
        t1.setUuid("uuid-t1");
        t1.setDate(odt);
        t2 = new Transaction();
        t2.setAmount("500");
        t2.setUuid("uuid-t2");
        t2.setDate(odt.minusDays(31));
        t3 = new Transaction();
        t3.setAmount("500");
        t3.setUuid("uuid-t3");
        t3.setDate(odt);
        pt1 = new PivotTransactionCard();
        pt2 = new PivotTransactionCard();
        pt3 = new PivotTransactionCard();
        TransactionCard tc1 = new TransactionCard();
        tc1.setTransaction(t1);
        tc1.setCard(c1);
        TransactionCard tc2 = new TransactionCard();
        tc2.setTransaction(t2);
        tc2.setCard(c1);
        TransactionCard tc3 = new TransactionCard();
        tc3.setTransaction(t3);
        tc3.setCard(c2);
        pt1.setTransactionCard(tc1);
        pt2.setTransactionCard(tc2);
        pt3.setTransactionCard(tc3);

    }

    @BeforeEach
    void beforeEachInit(){
        cardsRepository.save(c1);
        cardsRepository.save(c2);
        transactionsRepository.save(t1);
        transactionsRepository.save(t2);
        transactionsRepository.save(t3);
        transactionCardRepository.save(pt1);
        transactionCardRepository.save(pt2);
        transactionCardRepository.save(pt3);
    }

    @Test
    void findByTransactionCard_Card_CardNumberAndTransactionCard_Transaction_DateIsAfterTest(){
        var res = transactionCardRepository.findByTransactionCard_Card_CardNumberAndTransactionCard_Transaction_DateGreaterThanEqual(c1.getCardNumber(),odt.minusDays(30));
        assertEquals(1,res.size());
        assertEquals("uuid-t1",res.get(0).getTransactionCard().getTransaction().getUuid());

    }

    @Test
    void Transaction_Duplicate(){
        TransactionCard tc4 = new TransactionCard();
        tc4.setTransaction(t3);
        tc4.setCard(c1);
        PivotTransactionCard pt = new PivotTransactionCard();
        pt.setTransactionCard(tc4);
        Throwable exception = assertThrows(DataIntegrityViolationException.class, () -> {
            transactionCardRepository.save(pt);
        });
        assertThat(exception.getMessage(), containsString("UK"));
        assertThat(exception.getMessage(), containsString("PIVOT_CARD_TRANSACTION(TRANSACTION_UUID) VALUES"));

    }
}
