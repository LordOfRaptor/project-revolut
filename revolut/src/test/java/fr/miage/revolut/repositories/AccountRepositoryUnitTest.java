package fr.miage.revolut.repositories;


import fr.miage.revolut.entities.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("nosecurity")
class AccountRepositoryUnitTest {

    private static Account acc, a1;
    @Autowired
    AccountsRepository accountsRepository;

    @BeforeAll
    static void setUp() {
        acc = new Account();
        acc.setUuid("1");
        acc.setName("Name");
        acc.setSurname("Surname");
        acc.setCountry("France");
        acc.setPassport("123456789");
        acc.setPhoneNumber("+330909090909");
        acc.setIban("FR21315432464355");
        a1 = new Account();
        a1.setUuid("2");
        a1.setName("Name");
        a1.setSurname("Surname");
        a1.setCountry("France");
        a1.setPassport("123456789");
        a1.setPhoneNumber("+330909090909");
        a1.setIban("FR21315432464355");
    }

    @AfterEach
    void unset() {
        accountsRepository.deleteAll();
    }

    @Test
    void testSaveSucces() {
        assertDoesNotThrow(() -> accountsRepository.save(acc));
    }

    @Test
    void testNotUniquePhoneNumber() {
        a1.setPhoneNumber("+330909090909");
        a1.setPassport("123456779");
        a1.setIban("FR123456789");
        accountsRepository.save(acc);
        Throwable exception = assertThrows(DataIntegrityViolationException.class, () -> {
            accountsRepository.save(a1);
        });
        //Test UK for unique key
        assertThat(exception.getMessage(), containsString("UK"));
        //Test que c'est bien le numéro de telephone de la table account le probleme
        assertThat(exception.getMessage(), containsString("ACCOUNT(PHONE_NUMBER) VALUES"));

    }

    @Test
    void testNotUniquePassport() {
        a1.setPassport("123456789");
        a1.setPhoneNumber("+330908090909");
        a1.setIban("FR123456789");
        accountsRepository.save(acc);
        Throwable exception = assertThrows(DataIntegrityViolationException.class, () -> {
            accountsRepository.save(a1);
        });
        //Test UK for unique key
        assertThat(exception.getMessage(), containsString("UK"));
        //Test que c'est bien le numéro de passport de la table account le probleme
        assertThat(exception.getMessage(), containsString("ACCOUNT(PASSPORT) VALUES"));
    }

    @Test
    void testNotUniqueIban() {
        a1.setPhoneNumber("+330908090909");
        a1.setPassport("123456779");
        a1.setIban("FR21315432464355");
        accountsRepository.save(acc);
        Throwable exception = assertThrows(DataIntegrityViolationException.class, () -> {
            accountsRepository.save(a1);
        });
        //Test UK for unique key
        assertThat(exception.getMessage(), containsString("UK"));
        //Test que c'est bien le numéro de l'iban de la table account le probleme
        assertThat(exception.getMessage(), containsString("ACCOUNT(IBAN) VALUES"));

    }

    @Test
    void testUniqueAccount() {
        a1.setPhoneNumber("+330908090909");
        a1.setPassport("123456779");
        a1.setIban("FR123456789");
        accountsRepository.save(acc);
        assertDoesNotThrow(() -> accountsRepository.save(a1));
    }
}
