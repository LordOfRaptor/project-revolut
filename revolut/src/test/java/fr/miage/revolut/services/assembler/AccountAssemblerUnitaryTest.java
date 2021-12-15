package fr.miage.revolut.services.assembler;

import fr.miage.revolut.entities.Account;
import fr.miage.revolut.mapper.AccountsMapperImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountAssemblerUnitaryTest {

    private AccountAssembler accountAssembler;

    private static Account acc;

    @BeforeAll
    static void beforeTest(){
        acc = new Account();
        acc.setUuid("1");
        acc.setName("Name");
        acc.setSurname("Surname");
        acc.setCountry("France");
        acc.setPassport("123456789");
        acc.setPhoneNumber("+330909090909");
        acc.setIban("FR21315432464355");


    }

    @BeforeEach
    void beforeEach(){
        accountAssembler = new AccountAssembler(new AccountsMapperImpl());
    }
}
