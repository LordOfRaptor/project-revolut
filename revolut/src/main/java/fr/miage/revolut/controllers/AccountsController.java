package fr.miage.revolut.controllers;

import fr.miage.revolut.dto.create.NewAccount;
import fr.miage.revolut.dto.view.AccountView;
import fr.miage.revolut.entities.Account;
import fr.miage.revolut.services.AccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@ExposesResourceFor(Account.class)
public class AccountsController {

    private final AccountsService accountsService;
    private Logger LOGGER = Logger.getLogger(String.valueOf(AccountsController.class));

    @GetMapping(value = "/{uuidIntervenant}")
    public ResponseEntity<EntityModel<AccountView>> getOneAccount(@PathVariable("uuidIntervenant") String uuid){
        EntityModel<AccountView> accountView = accountsService.findAccount(uuid);
        return (accountView == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(accountView);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> saveAccount(@RequestBody @Valid NewAccount acc)  {
        URI location = accountsService.saveAccount(acc);
        return ResponseEntity.created(location).body(location);
    }

    @GetMapping(value = "/{uuidIntervenant}/cards")
    @Transactional
    public ResponseEntity<?> getAccountCards(@PathVariable("uuidIntervenant") String uuid)  {
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{uuidIntervenant}/transactions")
    @Transactional
    public ResponseEntity<?> getAccountTransactions(@PathVariable("uuidIntervenant") String uuid)  {
        return ResponseEntity.notFound().build();
    }

}
