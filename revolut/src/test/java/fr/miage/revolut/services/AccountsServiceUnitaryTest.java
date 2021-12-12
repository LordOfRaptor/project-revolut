package fr.miage.revolut.services;

import fr.miage.revolut.dto.view.AccountView;
import fr.miage.revolut.entities.Account;
import fr.miage.revolut.mapper.AccountsMapper;
import fr.miage.revolut.repositories.AccountsRepository;
import fr.miage.revolut.services.assembler.AccountAssembler;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;

import java.util.Optional;

@SpringBootTest
class AccountsServiceUnitaryTest {

    @MockBean
    AccountsRepository repository;


    @Autowired
    AccountsService accountsService;

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

    @Test
    void findAccountSucces(){
        when(repository.findById("1")).thenReturn(Optional.of(acc));
        //when(assembler.toModel(acc)).thenReturn(EntityModel.of(accountsMapper.toDto(acc)));
        
        EntityModel<AccountView> entityModelAcc = accountsService.findAccount("1");
        assertNotNull(entityModelAcc);
        assertNotNull(entityModelAcc.getContent());
        assertEquals("Name",entityModelAcc.getContent().getName());
    }

    @Test
    void findAccountFail(){
        EntityModel<AccountView> entityModelAcc = accountsService.findAccount("1");
        assertNull(entityModelAcc);
    }


}
