package fr.miage.revolut.controllers;

import fr.miage.revolut.dto.create.NewAccount;
import fr.miage.revolut.dto.view.AccountView;
import fr.miage.revolut.entities.Account;
import fr.miage.revolut.mapper.AccountsMapper;
import fr.miage.revolut.services.AccountsService;
import fr.miage.revolut.services.assembler.AccountAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@ExposesResourceFor(Account.class)
public class AccountsController {

    private final AccountsService accountsService;
    private final AccountAssembler accountsAssembler;
    private final AccountsMapper accountsMapper;
    private Logger LOGGER = Logger.getLogger(String.valueOf(AccountsController.class));

    @GetMapping(value = "/{uuidIntervenant}")
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
