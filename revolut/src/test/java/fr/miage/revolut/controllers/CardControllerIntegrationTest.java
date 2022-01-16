package fr.miage.revolut.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.miage.revolut.dto.UserRequest;
import fr.miage.revolut.dto.create.NewCard;
import fr.miage.revolut.dto.patch.PatchCard;
import fr.miage.revolut.entities.Account;
import fr.miage.revolut.entities.Card;
import fr.miage.revolut.repositories.AccountsRepository;
import fr.miage.revolut.repositories.CardsRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("security")
@WithMockUser
public class CardControllerIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    CardsController ca;

    @Value("${app.test.keycloackUser}")
    private String uuid;

    private String access_token = null;
    private static Account account;
    private static NewCard newCard;
    private static Card c1;
    private static String cardNumber;

    @Autowired
    private AccountsRepository ar;
    @Autowired
    private CardsRepository cr;

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

        newCard = new NewCard();
        newCard.setBlocked(false);
        newCard.setContactless(false);
        newCard.setLimit(1000);
        newCard.setLocation(false);
        newCard.setVirtual(false);

        c1 = new Card();
        c1.setCardNumber(cardNumber);
        c1.setAccount(account);
        c1.setDelete(false);

    }



    @BeforeEach
    public void setupContext(){
        account.setUuid(uuid);

        ar.save(account);
        cr.save(c1);
        RestAssured.port = port;
    }

    @AfterEach
    public  void unset(){
        cr.deleteAll();
        ar.deleteAll();

    }

    @Test
    void gellAllCard() throws Exception {
        var c2 = new Card();
        c2.setCardNumber("1234567891234568");
        c2.setAccount(account);
        c2.setDelete(false);
        cr.save(c2);
        Response response = given().header(getHeaderAuthorization())
                .when()
                .get("/accounts/"+ uuid +"/cards")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
       assertEquals(2,response.jsonPath().getList("_embedded.cardViewList").size());
    }

    @Test
    void getCardFailure() throws Exception{
        given().header(getHeaderAuthorization())
                .when()
                .get("/accounts/"+ uuid +"/cards/1234")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void deleteCard() throws Exception{
        given().header(getHeaderAuthorization())
                .when()
                .delete("/accounts/"+ uuid +"/cards/" +cardNumber)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
        given().header(getHeaderAuthorization())
                .when()
                .get("/accounts/"+ uuid +"/cards/" +cardNumber)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
        Response response = given().header(getHeaderAuthorization())
                .when()
                .get("/accounts/"+ uuid +"/cards")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
        assertNull(response.jsonPath().getList("_embedded.cardViewList"));
    }

    @Test
    void getCard() throws Exception{
        Response response = given().header(getHeaderAuthorization())
                .when()
                .get("/accounts/"+ uuid +"/cards/" +cardNumber)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
        assertEquals(cardNumber,response.jsonPath().get("cardNumber"));
    }

    @Test
    void patchCard() throws Exception{
        PatchCard card = new PatchCard();
        card.setLocation(true);
        Response response = given().header(getHeaderAuthorization())
                .body(this.toJsonString(card))
                .contentType(ContentType.JSON)
                .when()
                .patch("/accounts/"+ uuid +"/cards/" +cardNumber)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        assertTrue(cr.findById(cardNumber).get().getLocation());
    }


    @Test
    void postCard() throws Exception {
        Response response = given().header(getHeaderAuthorization())
                .body(this.toJsonString(newCard))
                .contentType(ContentType.JSON)
                .when()
                .post("/accounts/"+ uuid +"/cards")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .response();
    }

    private String toJsonString(Object o) throws Exception{
        ObjectMapper map = new ObjectMapper();
        return  map.writeValueAsString(o);

    }

    private Header getHeaderAuthorization() throws Exception {
        if(access_token == null) {
            UserRequest ur = new UserRequest();
            ur.setPassword("testtest");
            Response response = given().body(this.toJsonString(ur)).contentType(ContentType.JSON).
                    when().post("/accounts/" + uuid).then().extract().response();
            access_token = response.jsonPath().getString("access_token");
        }
        return new Header("Authorization","bearer "+ access_token);
    }
}
