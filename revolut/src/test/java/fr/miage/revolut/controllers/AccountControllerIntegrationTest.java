package fr.miage.revolut.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.miage.revolut.dto.UserRequest;
import fr.miage.revolut.dto.create.NewAccount;
import fr.miage.revolut.dto.patch.PatchAccount;
import fr.miage.revolut.entities.Account;
import fr.miage.revolut.repositories.AccountsRepository;
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

import java.math.BigDecimal;
import java.util.Optional;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("security")
@WithMockUser
class AccountControllerIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    AccountsController ac;

    private static Logger logger = Logger.getLogger(AccountControllerIntegrationTest.class.toString());

    @Value("${app.test.keycloackUser}")
    private String uuid;

    @Value("${app.test.keycloackUserForFailure}")
    private String uuidFail;

    @Autowired
    private AccountsRepository ar;

    private String access_token = null;
    private static Account account;
    private static NewAccount newAccount;

    @BeforeAll
    static void init(){
        account = new Account();
        account.setName("Noirot");
        account.setSurname("Quentin");
        account.setCountry("France");
        account.setPassport("ZZZRTUUIT");
        account.setPhoneNumber("+332689850908");
        account.setIban("FR04D95088E40A4B329E84");


        newAccount = new NewAccount();
        newAccount.setName("Noirot");
        newAccount.setSurname("Quentin");
        newAccount.setPhoneNumber("+330606060606");

        newAccount.setPassport("AAAAAAAAA");
        newAccount.setCountry("France");
        newAccount.setPassword("testtest");
        newAccount.setSolde(BigDecimal.valueOf(1000));
    }


    @BeforeEach
    public void setupContext(){
        //password is testest
        account.setUuid(uuid);
        newAccount.setPassport("AAAAAAAAA");
        ar.save(account);
        RestAssured.port = port;
    }

    @AfterEach
    public void deleteContext(){
        ar.deleteAll();
    }

    @Test
    public void postApiSucces() throws Exception {
        Response response = given().body(this.toJsonString(newAccount))
                .contentType(ContentType.JSON)
                .when()
                .post("/accounts")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .response();
        String location = response.getHeader("Location");
        UserRequest ur = new UserRequest();
        ur.setPassword("testtest");
        Response response2 = given().body(this.toJsonString(ur)).contentType(ContentType.JSON).
                when().post(location).then().statusCode(HttpStatus.SC_OK).extract().response();
        assertThat(response2.getBody().prettyPrint(), containsString("access_token"));
    }

    @Test
    void postApiFailureWrongDTO() throws Exception {
        newAccount.setPassport("AAAAAAAAAA");
        Response response = given().body(this.toJsonString(newAccount))
                .contentType(ContentType.JSON)
                .when()
                .post("/accounts")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .response();
    }

    @Test
    void postApiFailureDuplicate() throws Exception {
        newAccount.setPassport("ZZZRTUUIT");
        Response response = given().body(this.toJsonString(newAccount))
                .contentType(ContentType.JSON)
                .when()
                .post("/accounts")
                .then()
                .statusCode(HttpStatus.SC_CONFLICT)
                .extract()
                .response();
    }

    @Test
    void getOneUnauthorizedInexistantAccount(){
        when().get("/accounts/"+"1234").then().statusCode(HttpStatus.SC_UNAUTHORIZED);

    }

    @Test
    void getOneAuthorized() throws Exception {
        given().header(getHeaderAuthorization()).
                when().get("/accounts/"+uuid).then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    void getOneUnauthorizedWrongBearer() throws Exception {
        Header h =new Header("Authorization","bearer "+ "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJOeWFweHQ1LWM2WXBmWks2RWFya19UeU1uN25HdGM4aTgwczlFUVlnbGlNIn0.eyJleHAiOjE2NDE0NjE1NzYsImlhdCI6MTY0MTQ2MTI3NiwianRpIjoiN2IxZjAxOWYtZjgwMS00ZjZiLTliYTktNjkxMzk3YjY2ODYxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL2F1dGgvcmVhbG1zL2tleWNsb2FrLXNwcmluZyIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJhMDE4OTY4ZS1iNDRhLTQyMDAtOWM1Yi0yZGMwNzY5YWUxNzgiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJrZXljbG9hay1zcHJpbmdib290Iiwic2Vzc2lvbl9zdGF0ZSI6Ijg1NmUzMGM3LTg5MWYtNGFmOS1iMzNmLTM0NGVhMjgzZjMyNSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo4MDgwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiZGVmYXVsdC1yb2xlcy1rZXljbG9hay1zcHJpbmciLCJhY2NvdW50Il19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwic2lkIjoiODU2ZTMwYzctODkxZi00YWY5LWIzM2YtMzQ0ZWEyODNmMzI1IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJmMTJjNjU3MC03NTg2LTQxMWQtOGViYS0zNzAwMTc2MTcwZmIifQ.cnJRPU6edKpjenFP8qhm6tH93jNIQ0z79_dkDSezp8nMYsIxybIy43x4xieRHUIPeLHRp0BInWAF6gxLg4jr2F4B5-lKcOwZ5q55nszygh4NZvEK1C5O51LGVXX35VB5qUJqY-8EhARqrhJR0e3hFWT-y8aGKCvinRwRrVxZCipNcm-KFSR096yY7pL-p7tg_sdS1z8cnoQ4vUlb85gJxcU7ik9fyPR_V8Ki7ZHeUTD_4faNnTsU7fpaApDfz1tYnWiMr1neSAeJM1XWL8O6yOxED63OSFsPvfSsDB8HZryoAOXNi27Wvz-ezVbIoqas67omBoQoD2ZNTwPpQS1Ypw");
        given().header(h).
                when().get("/accounts/"+uuid).then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void getOneUnauthorizedWrongAccount() throws Exception {
        Header h =new Header("Authorization","bearer "+ getHeaderAuthorizationForFailure());
        given().header(h).
                when().get("/accounts/"+uuid).then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void patchOneValid() throws Exception {
        PatchAccount pa = new PatchAccount();
        pa.setPassport("BBBBBBBBB");
        pa.setPhoneNumber("+330909090909");
        given().header(getHeaderAuthorization()).
                body(this.toJsonString(pa)).
                contentType(ContentType.JSON).
                when().patch("/accounts/"+uuid).then().statusCode(HttpStatus.SC_OK);
        Optional<Account> a = ar.findById(uuid);
        assertEquals("BBBBBBBBB",a.get().getPassport());
        assertEquals("+330909090909",a.get().getPhoneNumber());
    }

    @Test
    void patchOneNotValid() throws Exception {
        PatchAccount pa = new PatchAccount();
        pa.setPassport("BBBBBBBBBBB");
        given().header(getHeaderAuthorization()).
                body(this.toJsonString(pa)).
                contentType(ContentType.JSON).
                when().patch("/accounts/"+uuid).then().statusCode(HttpStatus.SC_BAD_REQUEST);
        Optional<Account> a = ar.findById(uuid);
        assertEquals("ZZZRTUUIT",a.get().getPassport());
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

    private Header getHeaderAuthorizationForFailure() throws Exception {
        if(access_token == null) {
            UserRequest ur = new UserRequest();
            ur.setPassword("testtest");
            Response response = given().body(this.toJsonString(ur)).contentType(ContentType.JSON).
                    when().post("/accounts/" + uuidFail).then().extract().response();
            access_token = response.jsonPath().getString("access_token");
        }
        return new Header("Authorization","bearer "+ access_token);
    }
}
