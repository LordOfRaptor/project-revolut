package fr.miage.revolut.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.miage.revolut.dto.PaymentOnline;
import fr.miage.revolut.dto.PaymentPhysique;
import fr.miage.revolut.entities.Account;
import fr.miage.revolut.entities.Card;
import fr.miage.revolut.repositories.AccountsRepository;
import fr.miage.revolut.repositories.CardsRepository;
import fr.miage.revolut.repositories.TransactionCardRepository;
import fr.miage.revolut.repositories.TransactionsRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("security")
@WithMockUser
public class PaymentControllerIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    PaymentController paymentController;

    @Autowired
    private AccountsRepository ar;
    @Autowired
    private CardsRepository cr;

    @Autowired
    private TransactionCardRepository tcr;

    @Autowired
    private TransactionsRepository tr;

    @Value("${app.test.keycloackUser}")
    private String uuid;

    private static Account account;
    private static Card c1;
    private static String cardNumber;
    private static PaymentOnline paymentOnline;
    private static PaymentPhysique paymentPhysique;

    @BeforeAll
    static void init(){
        cardNumber = "1234567891234567";
        account = new Account();
        account.setName("Noirot");
        account.setSurname("Quentin");
        account.setCountry("France");
        account.setPassport("ZZZRTUUIT");
        account.setPhoneNumber("+332689850908");
        account.setIban("FR04D95088E40A4B329E84");
        account.setPassport("AAAAAAAAA");
        account.setSolde("100000");



    }



    @BeforeEach
    public void setupContext(){
        account.setUuid(uuid);
        c1 = new Card();
        c1.setCardNumber(cardNumber);
        c1.setContactless(false);
        c1.setLocation(false);
        c1.setCvv("123");
        c1.setLimit(500);
        c1.setAccount(account);
        c1.setDelete(false);
        c1.setCode("1234");
        c1.setVirtual(false);
        c1.setBlocked(false);
        paymentOnline = new PaymentOnline();
        paymentOnline.setCountry("France");
        paymentOnline.setCardNumber(cardNumber);
        paymentOnline.setCvv("123");
        paymentOnline.setCreditAccount("1234");
        paymentOnline.setAmount(BigDecimal.valueOf(10));
        paymentPhysique = new PaymentPhysique();
        paymentPhysique.setContacless(false);
        paymentPhysique.setCountry("France");
        paymentPhysique.setCode("1234");
        paymentPhysique.setAmount(BigDecimal.valueOf(10));
        paymentPhysique.setCreditAccount("1234");
        paymentPhysique.setCardNumber(cardNumber);

        ar.save(account);
        cr.save(c1);
        RestAssured.port = port;
    }

    @AfterEach
    public  void unset(){
        tcr.deleteAll();
        cr.deleteAll();
        ar.deleteAll();

    }

    @Test
    void paymentTransactionOnline() throws Exception{
        given()
                .body(this.toJsonString(paymentOnline))
                .contentType(ContentType.JSON)
                .when()
                .post("/paiement/online")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    void paymentTransactionOnlineFailure() throws Exception{
        paymentOnline.setCountry("England");
        c1.setCardNumber(cardNumber);
        c1.setLocation(true);
        cr.save(c1);
        given()
                .body(this.toJsonString(paymentOnline))
                .contentType(ContentType.JSON)
                .when()
                .post("/paiement/online")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void paymentTransactionOnlineFailureCardNumber() throws Exception{
        paymentOnline.setCardNumber("9876543211234567");
        given()
                .body(this.toJsonString(paymentOnline))
                .contentType(ContentType.JSON)
                .when()
                .post("/paiement/online")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void paymentTransactionOnlineFailureCvv() throws Exception{
        paymentOnline.setCvv("321");
        given()
                .body(this.toJsonString(paymentOnline))
                .contentType(ContentType.JSON)
                .when()
                .post("/paiement/online")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void paymentTransactionOnlineVirtual() throws Exception{
        c1.setVirtual(true);
        cr.save(c1);
        given()
                .body(this.toJsonString(paymentOnline))
                .contentType(ContentType.JSON)
                .when()
                .post("/paiement/online")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
        assertTrue(cr.findById(cardNumber).get().getDelete());

    }

    @Test
    void paymentTransactionOnlineFailureLimit() throws Exception{
        paymentOnline.setAmount(BigDecimal.valueOf(501));
        given()
                .body(this.toJsonString(paymentOnline))
                .contentType(ContentType.JSON)
                .when()
                .post("/paiement/online")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);

    }

    @Test
    void paymentTransactionOnlineFailureBlocked() throws Exception{
        c1.setBlocked(true);
        cr.save(c1);
        given()
                .body(this.toJsonString(paymentOnline))
                .contentType(ContentType.JSON)
                .when()
                .post("/paiement/online")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void paymentTransactionPhysique() throws Exception{
        given()
                .body(this.toJsonString(paymentPhysique))
                .contentType(ContentType.JSON)
                .when()
                .post("/paiement/physique")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    void paymentTransactionPhysiqueFailureCountry() throws Exception{
        paymentPhysique.setCountry("Enlgish");
        c1.setLocation(true);
        cr.save(c1);
        given()
                .body(this.toJsonString(paymentPhysique))
                .contentType(ContentType.JSON)
                .when()
                .post("/paiement/physique")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void paymentTransactionPhysiqueFailureNumberCard() throws Exception{
        paymentPhysique.setCardNumber("9876543211234567");
        given()
                .body(this.toJsonString(paymentPhysique))
                .contentType(ContentType.JSON)
                .when()
                .post("/paiement/physique")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void paymentTransactionPhysiqueFailureCode() throws Exception{
        paymentPhysique.setCode("4321");
        given()
                .body(this.toJsonString(paymentPhysique))
                .contentType(ContentType.JSON)
                .when()
                .post("/paiement/physique")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void paymentTransactionPhysiqueVirtual() throws Exception{
        c1.setVirtual(true);
        cr.save(c1);
        given()
                .body(this.toJsonString(paymentPhysique))
                .contentType(ContentType.JSON)
                .when()
                .post("/paiement/physique")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
        assertTrue(cr.findById(cardNumber).get().getDelete());

    }

    @Test
    void paymentTransactionPhysiqueFailureLimit() throws Exception{
        paymentPhysique.setAmount(BigDecimal.valueOf(501));
        given()
                .body(this.toJsonString(paymentPhysique))
                .contentType(ContentType.JSON)
                .when()
                .post("/paiement/physique")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);

    }

    @Test
    void paymentTransactionPhysiqueFailureBlocked() throws Exception{
        c1.setBlocked(true);
        cr.save(c1);
        given()
                .body(this.toJsonString(paymentPhysique))
                .contentType(ContentType.JSON)
                .when()
                .post("/paiement/physique")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void paymentTransactionPhysiqueFailureContactLess() throws Exception{
        paymentPhysique.setContacless(true);
        given()
                .body(this.toJsonString(paymentPhysique))
                .contentType(ContentType.JSON)
                .when()
                .post("/paiement/physique")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void paymentTransactionPhysiqueContactLess() throws Exception{
        paymentPhysique.setContacless(true);
        c1.setContactless(true);
        cr.save(c1);
        given()
                .body(this.toJsonString(paymentPhysique))
                .contentType(ContentType.JSON)
                .when()
                .post("/paiement/physique")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }





    private String toJsonString(Object o) throws Exception{
        ObjectMapper map = new ObjectMapper();
        return  map.writeValueAsString(o);

    }
}
