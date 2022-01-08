package fr.miage.revolut.controllers;

import fr.miage.revolut.config.annotations.IsUser;
import fr.miage.revolut.dto.UserRequest;
import fr.miage.revolut.dto.create.NewAccount;
import fr.miage.revolut.dto.view.AccountView;
import fr.miage.revolut.entities.Account;
import fr.miage.revolut.mapper.AccountsMapper;
import fr.miage.revolut.services.AccountsService;
import fr.miage.revolut.services.assembler.AccountAssembler;
import lombok.RequiredArgsConstructor;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@ExposesResourceFor(Account.class)
public class AccountsController {

    private final Logger log = Logger.getLogger(AccountsController.class.toString());
    private final AccountsService accountsService;
    private final AccountAssembler accountsAssembler;
    private final AccountsMapper accountsMapper;

    @Value("${keycloak.auth-server-url}")
    private String authUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${keycloak.credentials.secret}")
    private String secret;



    private Logger LOGGER = Logger.getLogger(String.valueOf(AccountsController.class));

    //@PreAuthorize(value = "hasRole('user') and authentication.name.equals(#uuid)")
    @GetMapping(value = "/{uuidIntervenant}")
    @IsUser
    public ResponseEntity<EntityModel<AccountView>> getOneAccount(@PathVariable("uuidIntervenant") String uuid) {
        var a = accountsService.findAccount(uuid);
        return Optional.ofNullable(a).filter(Optional::isPresent)
                .map(account -> ResponseEntity.ok(accountsAssembler.toModel(account.get())))
                .orElse(ResponseEntity.notFound().build());


    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> saveAccount(@RequestBody @Valid NewAccount acc) {
        Account a = accountsService.saveAccount(acc);
        URI location = linkTo(AccountsController.class).slash(a.getUuid()).toUri();
        return ResponseEntity.created(location).body(accountsMapper.toDto(a));
    }

    @PostMapping(path = "/{uuidIntervenant}")
    public ResponseEntity<?> signin(@PathVariable("uuidIntervenant") String uuid,@RequestBody UserRequest userRequest){
        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put("secret", secret);
        clientCredentials.put("grant_type", "password");

        Configuration configuration =
                new Configuration(authUrl, realm, clientId, clientCredentials, null);
        AuthzClient authzClient = AuthzClient.create(configuration);

        AccessTokenResponse response =
                authzClient.obtainAccessToken(uuid, userRequest.getPassword());

        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "/{uuidIntervenant}")
    @IsUser
    public ResponseEntity<EntityModel<AccountView>> patchAccount(@PathVariable("uuidIntervenant") String uuid,@RequestBody Map<Object, Object> fields) {
        var a = accountsService.patchAccount(uuid,fields);
        return Optional.ofNullable(a).filter(Optional::isPresent)
                .map(account -> ResponseEntity.ok(accountsAssembler.toModel(account.get())))
                .orElse(ResponseEntity.notFound().build());


    }

    @GetMapping(value = "/{uuidIntervenant}/cards")
    @Transactional
    public ResponseEntity<?> getAccountCards(@PathVariable("uuidIntervenant") String uuid) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{uuidIntervenant}/transactions")
    @Transactional
    public ResponseEntity<?> getAccountTransactions(@PathVariable("uuidIntervenant") String uuid) {
        return ResponseEntity.notFound().build();
    }

}
