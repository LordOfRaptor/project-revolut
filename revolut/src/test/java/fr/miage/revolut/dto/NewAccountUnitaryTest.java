package fr.miage.revolut.dto;


import fr.miage.revolut.dto.create.NewAccount;
import fr.miage.revolut.services.validator.NewAccountValidator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class NewAccountUnitaryTest {

    static private Validator validator;
    static private NewAccount newAccount;
    private NewAccountValidator newAccountValidator;

    @BeforeAll
    static void setUpAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {
        newAccount = new NewAccount();
        newAccount.setName("Luc");
        newAccount.setSurname("Tristan");
        newAccount.setCountry("France");
        newAccount.setPassport("AZ1234567");
        newAccount.setPhoneNumber("+330707070707");
        newAccount.setPassword("azertyuiop");
        newAccount.setSolde(BigDecimal.valueOf(1000));
        newAccountValidator = new NewAccountValidator(validator);
    }

    @Test
    void newAccountValidTest() {
        assertDoesNotThrow(() -> newAccountValidator.validate(newAccount));
    }

    @Nested
    class PhoneNumber {

        @DisplayName("Test regex phone number (too long/too short/withoutPlus/ZeroAtBeginning)")
        @ParameterizedTest
        @ValueSource(strings =
                {
                        "+0330707070707",
                        "330707070707",
                        "+3307077",
                        "+330707070707077",
                        "+33070707+0707"
                })
        void newAccountPhoneNumberNotValidTest(String value) {
            newAccount.setPhoneNumber(value);
            Throwable exception = assertThrows(ConstraintViolationException.class,
                    () -> newAccountValidator.validate(newAccount));
            assertThat(exception.getMessage(), containsString("Numero de telephone invalide"));
        }

        @DisplayName("Test regex phone number valid (MaxSize/MinSize)")
        @ParameterizedTest
        @ValueSource(strings =
                {
                        "+33070707",
                        "+33070707070707"
                })
        void newAccountPhoneNumberValidTest(String value) {
            newAccount.setPhoneNumber(value);
            assertDoesNotThrow(() -> newAccountValidator.validate(newAccount));
        }
    }

    @Nested
    class Passport {

        @DisplayName("Test regex passport (too long/too short/letterNotCapital)")
        @ParameterizedTest
        @ValueSource(strings =
                {
                        "FR12345678",
                        "FR123456",
                        "Fr1234567"
                })
        void newAccountPassportNotValidTest(String value) {
            newAccount.setPassport(value);
            Throwable exception = assertThrows(ConstraintViolationException.class,
                    () -> newAccountValidator.validate(newAccount));
            assertThat(exception.getMessage(), containsString("Numero de passport invalide"));
        }

        @DisplayName("Test regex phone number valid (number/letter)")
        @ParameterizedTest
        @ValueSource(strings =
                {
                        "FR1234567",
                        "ABCDEFGHI",
                        "123456789"
                })
        void newAccountPassportValidTest(String value) {
            newAccount.setPassport(value);
            assertDoesNotThrow(() -> newAccountValidator.validate(newAccount));
        }
    }

    @Nested
    class Password {

        @DisplayName("Test regex paswword (too long/too short)")
        @ParameterizedTest
        @ValueSource(strings =
                {
                        "azertyu",
                        "azertyazertyazertyazertyazertya"
                })
        void newAccountPasswordNotValidTest(String value) {
            newAccount.setPassword(value);
            Throwable exception = assertThrows(ConstraintViolationException.class,
                    () -> newAccountValidator.validate(newAccount));
            assertThat(exception.getMessage(), containsString("password invalid"));
        }

        @DisplayName("Test regex phone number valid (longer/shorter)")
        @ParameterizedTest
        @ValueSource(strings =
                {
                        "azertyui",
                        "azertyazertyazertyazertyazerty"
                })
        void newAccountPasswordValidTest(String value) {
            newAccount.setPassword(value);
            assertDoesNotThrow(() -> newAccountValidator.validate(newAccount));
        }
    }

    @Nested
    class Blanked {

        @DisplayName("Test regex surname (blank/empty/too short)")
        @ParameterizedTest
        @ValueSource(strings =
                {
                        "  ",
                        "",
                        "s",
                })
        void newAccountSurnameNotValidTest(String value) {
            newAccount.setSurname(value);
            Throwable exception = assertThrows(ConstraintViolationException.class,
                    () -> newAccountValidator.validate(newAccount));
            assertThat(exception.getMessage(), containsString("surname invalid"));
        }

        @DisplayName("Test regex name (blank/empty/too short)")
        @ParameterizedTest
        @ValueSource(strings =
                {
                        "  ",
                        "",
                        "s",
                })
        void newAccountNameNotValidTest(String value) {
            newAccount.setName(value);
            Throwable exception = assertThrows(ConstraintViolationException.class,
                    () -> newAccountValidator.validate(newAccount));
            assertThat(exception.getMessage(), containsString("name invalid"));
        }

        @DisplayName("Test regex country (blank/empty/too short)")
        @ParameterizedTest
        @ValueSource(strings =
                {
                        "    ",
                        "",
                        "Oma",
                })
        void newAccountCountryNotValidTest(String value) {
            newAccount.setCountry(value);
            Throwable exception = assertThrows(ConstraintViolationException.class,
                    () -> newAccountValidator.validate(newAccount));
            assertThat(exception.getMessage(), containsString("country invalid"));
        }

        @Test
        void newAccountCountrySurnameNameValidTest() {
            newAccount.setCountry("Oman");
            newAccount.setSurname("Si");
            newAccount.setName("Ho");
            assertDoesNotThrow(() -> newAccountValidator.validate(newAccount));
        }

    }
}
