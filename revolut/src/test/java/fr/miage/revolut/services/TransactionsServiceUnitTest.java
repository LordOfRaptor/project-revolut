package fr.miage.revolut.services;

import fr.miage.revolut.repositories.AccountsRepository;
import fr.miage.revolut.repositories.TransactionsRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class TransactionsServiceUnitTest {

    private TransactionService transactionService;
    @Mock
    private TransactionsRepository transactionsRepository;
    @Mock
    private AccountsRepository accountsRepository;

    @BeforeEach
    void init(){
        transactionService = new TransactionService(transactionsRepository,accountsRepository);
    }

}
