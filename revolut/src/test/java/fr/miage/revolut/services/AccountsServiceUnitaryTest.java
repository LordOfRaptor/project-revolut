package fr.miage.revolut.services;

import fr.miage.revolut.dto.create.NewAccount;
import fr.miage.revolut.entities.Account;
import fr.miage.revolut.mapper.AccountsMapperImpl;
import fr.miage.revolut.repositories.AccountsRepository;
import fr.miage.revolut.services.security.KeycloakService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//@ExtendWith(SpringExtension.class)
@ExtendWith({MockitoExtension.class, SpringExtension.class})
class AccountsServiceUnitaryTest {


    private static Account acc;
    private static NewAccount newAccount;
    @Mock
    AccountsRepository repository;
    @Mock
    KeycloakService keycloakService;
    AccountsService accountsService;

    @BeforeAll
    static void beforeTest() {
        acc = new Account();
        acc.setUuid("1");
        acc.setName("Name");
        acc.setSurname("Surname");
        acc.setCountry("France");
        acc.setPassport("123456789");
        acc.setPhoneNumber("+330909090909");
        acc.setIban("FR21315432464355");
        newAccount = new NewAccount();
        newAccount.setName("Luc");
        newAccount.setSurname("Tristan");
        newAccount.setCountry("France");
        newAccount.setPassport("AZ1234567");
        newAccount.setPhoneNumber("+330707070707");


    }

    @BeforeEach
    void beforeEach() {
        accountsService = new AccountsService(repository, new AccountsMapperImpl(),keycloakService);
    }

    @Test
    void findAccountSucces() {
        when(repository.findById("1")).thenReturn(Optional.of(acc));

        Optional<Account> acc = accountsService.findAccount("1");
        assertTrue(acc.isPresent());
    }

    @Test
    void findAccountFail() {
        Optional<Account> acc = accountsService.findAccount("1");
        assertFalse(acc.isPresent());
    }

    @Test
    @DisplayName("Verifie la bonen generation de l'iban")
    void saveAccountIban() {
        when(repository.save(any(Account.class))).thenAnswer(i -> i.getArguments()[0]);
        Account acc = accountsService.saveAccount(newAccount);
        //2 premiere lettre du pays
        String country = acc.getCountry().substring(0, 2).toUpperCase(Locale.ROOT);
        String iban = acc.getIban();
        assertEquals(country, iban.substring(0, 2));
        assertEquals(22, iban.length());
    }


}
