package fr.miage.revolut.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.miage.revolut.controllers.feignClients.ConversionClient;
import fr.miage.revolut.dto.UserRequest;
import fr.miage.revolut.dto.create.NewCard;
import fr.miage.revolut.dto.create.NewTransaction;
import fr.miage.revolut.dto.patch.PatchTransaction;
import fr.miage.revolut.entities.Account;
import fr.miage.revolut.entities.Card;
import fr.miage.revolut.entities.Transaction;
import fr.miage.revolut.repositories.AccountsRepository;
import fr.miage.revolut.repositories.TransactionsRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("security")
@WithMockUser
public class TransactionControllerIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    TransactionsController ca;

    @Mock
    ConversionClient conversionClient;

    @Value("${app.test.keycloackUser}")
    private String uuid;

    private String access_token = null;
    private static Account account;

    @Autowired
    private AccountsRepository ar;

    @Autowired
    private TransactionsRepository tr;

    private static Transaction tr1,tr2;

    private static NewTransaction ntr1;

    @BeforeAll
    static void init(){
        account = new Account();
        account.setName("Noirot");
        account.setSurname("Quentin");
        account.setCountry("France");
        account.setPassport("ZZZRTUUIT");
        account.setPhoneNumber("+332689850908");
        account.setIban("FR04D95088E40A4B329E84");
        account.setPassport("AAAAAAAAA");
        account.setSolde("10000");

        ntr1 = new NewTransaction();
        ntr1.setCountry("France");
        ntr1.setAmount(BigDecimal.valueOf(10.5));
        ntr1.setCreditAccount("1234");
        tr1 = new Transaction();
        tr1.setUuid("1111");
        tr1.setCountry("France");
        tr1.setDebtorAccount("FR04D95088E40A4B329E84");
        tr1.setCreditAccount("1234");
        tr1.setDate(OffsetDateTime.now());
        tr1.setChangeRate(BigDecimal.ONE);
        tr1.setLabel("restaurant");
        tr1.setAmount("500");
        tr2 = new Transaction();
        tr2.setUuid("2222");
        tr2.setCountry("France");
        tr2.setDebtorAccount("1234");
        tr2.setAmount("500");
        tr2.setCreditAccount("FR04D95088E40A4B329E84");
        tr2.setDate(OffsetDateTime.now());
        tr2.setChangeRate(BigDecimal.ONE);
        tr2.setLabel("loyer");

    }



    @BeforeEach
    public void setupContext(){
        account.setUuid(uuid);
        ntr1.setAmount(BigDecimal.valueOf(10.5));

        ar.save(account);
        tr.save(tr1);
        tr.save(tr2);
        RestAssured.port = port;
    }

    @AfterEach
    public  void unset(){
        tr.deleteAll();
        ar.deleteAll();

    }

    @Test
    void getAllTransactions() throws Exception {
        Response response = given().header(getHeaderAuthorization())
                .when()
                .get("/accounts/"+ uuid +"/transactions")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
        assertEquals(2,response.jsonPath().getList("_embedded.transactionViewList").size());
        assertEquals(-5,(int)response.jsonPath().get("_embedded.transactionViewList[0].amount"));
        assertEquals(5,(int)response.jsonPath().get("_embedded.transactionViewList[1].amount"));
    }

    @Test
    void getAllTransactionsQuery() throws Exception {
        Response response = given().header(getHeaderAuthorization())
                .queryParam("label","loyer")
                .when()
                .get("/accounts/"+ uuid +"/transactions")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
        assertEquals(1,response.jsonPath().getList("_embedded.transactionViewList").size());
    }



    @Test
    void getTransaction() throws Exception{
        Response response = given().header(getHeaderAuthorization())
                .when()
                .get("/accounts/"+ uuid +"/transactions/"+tr1.getUuid())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
        assertEquals(-5,(int)response.jsonPath().get("amount"));
    }

    @Test
    void getTransactionChangeRate() throws Exception{
        var tr3 = new Transaction();

        tr3.setUuid("3333");
        tr3.setCountry("France");
        tr3.setDebtorAccount("1234");
        tr3.setAmount("500");
        tr3.setCreditAccount("FR04D95088E40A4B329E84");
        tr3.setDate(OffsetDateTime.now());
        tr3.setChangeRate(BigDecimal.valueOf(1.2));
        tr3.setLabel("loyer");
        tr.save(tr3);

        Response response = given().header(getHeaderAuthorization())
                .when()
                .get("/accounts/"+ uuid +"/transactions/"+tr3.getUuid())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
        assertEquals(6,(int)response.jsonPath().get("amount"));
    }

    @Test
    void getTransactionFailure() throws Exception{
        given().header(getHeaderAuthorization())
                .queryParam("label","loyer")
                .when()
                .get("/accounts/"+ uuid +"/transactions/12345")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void patchTransaction() throws Exception{
        PatchTransaction pt = new PatchTransaction();
        pt.setDebtorAccountName("me");
        Response response = given().header(getHeaderAuthorization())
                .body(this.toJsonString(pt))
                .contentType(ContentType.JSON)
                .when()
                .patch("/accounts/"+ uuid +"/transactions/"+tr1.getUuid())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
        assertEquals("me",response.jsonPath().get("debtorAccountName"));
    }

    @Test
    void patchTransactionFailureDebtorIban() throws Exception{
        PatchTransaction pt = new PatchTransaction();
        pt.setDebtorAccountName("me");
        given().header(getHeaderAuthorization())
                .body(this.toJsonString(pt))
                .contentType(ContentType.JSON)
                .when()
                .patch("/accounts/"+ uuid +"/transactions/"+tr2.getUuid())
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void postTransaction() throws Exception{
        Response response = given().header(getHeaderAuthorization())
                .body(this.toJsonString(ntr1))
                .contentType(ContentType.JSON)
                .when()
                .post("/accounts/"+ uuid +"/transactions")
                .then()
                .statusCode(HttpStatus.SC_CREATED).extract().response();
        String location = response.getHeader("Location");
        response = given().header(getHeaderAuthorization())
                .when()
                .get(location)
                .then()
                .statusCode(HttpStatus.SC_OK).extract().response();
        assertEquals((float)-10.5,(float)response.jsonPath().get("amount"));
    }

    @Test
    void postTransactionFailure() throws Exception{
        ntr1.setAmount(BigDecimal.valueOf(100.01));
        given().header(getHeaderAuthorization())
                .body(this.toJsonString(ntr1))
                .contentType(ContentType.JSON)
                .when()
                .post("/accounts/"+ uuid +"/transactions")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
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
