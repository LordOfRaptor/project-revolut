package fr.miage.revolut.services;

import fr.miage.revolut.controllers.feignClients.ConversionClient;
import fr.miage.revolut.dto.create.NewTransaction;
import fr.miage.revolut.entities.Account;
import fr.miage.revolut.entities.Transaction;
import fr.miage.revolut.mapper.TransactionsMapperImpl;
import fr.miage.revolut.repositories.AccountsRepository;
import fr.miage.revolut.repositories.TransactionsRepository;
import fr.miage.revolut.services.validator.PatchTransactionValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class TransactionsServiceUnitTest {

    private TransactionService transactionService;
    @Mock
    private TransactionsRepository transactionsRepository;
    @Mock
    private AccountsRepository accountsRepository;

    @Mock
    private ConversionClient conversionClient;
    private static PatchTransactionValidator patchTransactionValidator;

    private static Account acc,acc2;
    private static Transaction t1,t2;
    private static NewTransaction newTransaction;
    private static String iban="FR21315432464355";
    private static String iban2="FR123456789";

    @BeforeAll
    static void init(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        patchTransactionValidator = new PatchTransactionValidator(validator);
    }

    @BeforeEach
    void set(){
        newTransaction = new NewTransaction();
        newTransaction.setAmount(new BigDecimal("10.50"));
        newTransaction.setCountry("France");
        newTransaction.setCreditAccount(iban2);
        acc = new Account();
        acc.setUuid("1");
        acc.setName("Name");
        acc.setSurname("Surname");
        acc.setCountry("France");
        acc.setPassport("123456789");
        acc.setPhoneNumber("+330909090909");
        acc.setSolde("5000");
        acc.setIban(iban);
        acc2 = new Account();
        acc2.setUuid("2");
        acc2.setName("Name");
        acc2.setSurname("Surname");
        acc2.setCountry("France");
        acc2.setPassport("123456789");
        acc2.setPhoneNumber("+330909090909");
        acc2.setIban(iban2);
        acc2.setSolde("5000");
        t1 = new Transaction();
        t1.setAmount("500");
        t1.setCreditAccount(iban);
        t1.setDebtorAccount("5678");
        t1.setCountry("France");
        t1.setUuid("uuid-t1");
        t1.setChangeRate(new BigDecimal("1.2"));
        t1.setLabel("Paiement restaurant");
        t1.setCategory("Restaurant");
        t2 = new Transaction();
        t2.setAmount("500");
        t2.setCreditAccount("5678");
        t2.setDebtorAccount(iban);
        t2.setCountry("France");
        t2.setUuid("uuid-t2");
        t2.setChangeRate(new BigDecimal("1"));
        t2.setLabel("Paiement loyer");
        t2.setCategory("Loyer");
        transactionService = new TransactionService(transactionsRepository,accountsRepository, patchTransactionValidator,new TransactionsMapperImpl(),conversionClient);
    }


    @Test
    void findAllTransactions(){
        when(accountsRepository.findById("1")).thenReturn(Optional.of(acc));
        when(transactionsRepository.findTransactionByIbanLabelCountryCategory(acc.getIban(),null,null,null)
        ).thenReturn(List.of(t1,t2));
        var res = transactionService.findAllTransactions("1", Collections.emptyMap());
        assertEquals("600",res.get(0).getAmount());
        assertEquals("-500",res.get(1).getAmount());

    }

    @Test
    void findAllTransactionsEmpty(){
        when(accountsRepository.findById("2")).thenReturn(Optional.ofNullable(null));
        var res = transactionService.findAllTransactions("2", Collections.emptyMap());
        assertEquals(0,res.size());

    }

    @Test
    void findTransaction(){
        when(accountsRepository.findById("1")).thenReturn(Optional.of(acc));
        when(transactionsRepository.findByUuidAndCreditAccountOrDebtorAccount(t2.getUuid(),iban)).thenReturn(Optional.ofNullable(t2));
        var res = transactionService.findTransaction("1",t2.getUuid());
        assertTrue(res.isPresent());
        assertEquals("-500",res.get().getAmount());

    }

    @Test
    void findTransactionEmpty(){
        when(accountsRepository.findById("2")).thenReturn(Optional.ofNullable(null));
        var res = transactionService.findTransaction("2",t2.getUuid());
        assertFalse(res.isPresent());

    }

    @Test
    void createTransaction(){
        when(accountsRepository.findById("1")).thenReturn(Optional.of(acc));
        when(accountsRepository.existsByIban(iban2)).thenReturn(true);
        when(accountsRepository.findByIban(iban2)).thenReturn(Optional.of(acc2));
        when(transactionsRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);
        var res = transactionService.create(newTransaction,"1");
        assertEquals(0,res.get().getChangeRate().compareTo(BigDecimal.ONE));
        assertEquals("6050",acc2.getSolde());
        assertEquals("3950",acc.getSolde());

    }

    @Test
    void createTransactionChangeRate(){
        newTransaction.setCountry("France");
        acc.setCountry("England");
        when(accountsRepository.findById("1")).thenReturn(Optional.of(acc));
        when(conversionClient.getConversion("England","France")).thenReturn(new BigDecimal("1.2"));
        when(accountsRepository.existsByIban(iban2)).thenReturn(true);
        when(accountsRepository.findByIban(iban2)).thenReturn(Optional.of(acc2));
        when(transactionsRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);
        var res = transactionService.create(newTransaction,"1");
        assertEquals(0,res.get().getChangeRate().compareTo(new BigDecimal("1.2")));
        assertEquals("6260",acc2.getSolde());
        assertEquals("3950",acc.getSolde());

    }

}
