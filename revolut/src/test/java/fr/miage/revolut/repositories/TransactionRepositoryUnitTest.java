package fr.miage.revolut.repositories;

import fr.miage.revolut.entities.Transaction;
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
public class TransactionRepositoryUnitTest {

    @Autowired
    private TransactionsRepository transactionsRepository;

    private static String iban = "1234";

    private static Transaction t1,t2,t3;

    @BeforeAll
    static void setUp() {
        t1 = new Transaction();
        t1.setAmount("500");
        t1.setCreditAccount(iban);
        t1.setDebtorAccount("5678");
        t1.setCountry("France");
        t1.setUuid("uuid-t1");
        t1.setLabel("Paiement restaurant");
        t1.setCategory("Restaurant");
        t2 = new Transaction();
        t2.setAmount("500");
        t2.setCreditAccount("5678");
        t2.setDebtorAccount(iban);
        t2.setCountry("France");
        t2.setUuid("uuid-t2");
        t2.setLabel("Paiement loyer");
        t2.setCategory("Loyer");
        t3 = new Transaction();
        t3.setAmount("500");
        t3.setCreditAccount("6789");
        t3.setDebtorAccount("5678");
        t3.setCountry("France");
        t3.setUuid("uuid-t3");
        t3.setLabel("Paiement course");
        t3.setCategory("Course");
    }

    @BeforeEach
    void init(){
        transactionsRepository.save(t1);
        transactionsRepository.save(t2);
        transactionsRepository.save(t3);
    }

    @AfterEach
    void uninit(){
        transactionsRepository.deleteAll();
    }

    @Test
    void findByTransactionByFilters(){
        var res = transactionsRepository.findTransactionByIbanLabelCountryCategory(iban,"restaurant",null,null);
        assertEquals(1,res.size());
        assertEquals("uuid-t1",res.get(0).getUuid());
        res = transactionsRepository.findTransactionByIbanLabelCountryCategory(iban,null,null,null);
        assertEquals(2,res.size());
        res = transactionsRepository.findTransactionByIbanLabelCountryCategory(iban,null,"belgique",null);
        assertEquals(0,res.size());

    }

    @Test
    void findByTransactionByFiltersLike(){
        var res = transactionsRepository.findTransactionByIbanLabelCountryCategory(iban,null,null,"loyer");
        assertEquals(1,res.size());
        assertEquals("uuid-t2",res.get(0).getUuid());

    }
    
    @Test
    void findTransactionFailure(){
        var res = transactionsRepository.findByUuidAndCreditAccountOrDebtorAccount("uuid-t3",iban);
        assertFalse(res.isPresent());

    }

    @Test
    void findTransactionSuccesDebtor(){
        var res = transactionsRepository.findByUuidAndCreditAccountOrDebtorAccount("uuid-t2",iban);
        assertTrue(res.isPresent());

    }

    @Test
    void findTransactionSuccesCredit(){
        var res = transactionsRepository.findByUuidAndCreditAccountOrDebtorAccount("uuid-t1",iban);
        assertTrue(res.isPresent());

    }


}
