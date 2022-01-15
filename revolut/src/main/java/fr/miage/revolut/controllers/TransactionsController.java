package fr.miage.revolut.controllers;

import fr.miage.revolut.config.annotations.IsUser;
import fr.miage.revolut.dto.create.NewTransaction;
import fr.miage.revolut.entities.Transaction;
import fr.miage.revolut.services.TransactionService;
import fr.miage.revolut.services.assembler.TransactionAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/accounts/{uuid}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@ExposesResourceFor(Transaction.class)
public class TransactionsController {

    private final TransactionService transactionService;
    private final TransactionAssembler transactionAssembler;

    @GetMapping
    @IsUser
    public ResponseEntity<?> getTransactions(@PathVariable("uuid") String uuid,@RequestParam(required = false) Map<String,String> filtres){
        return ResponseEntity.ok(transactionAssembler.toCollectionModelWithAccount(transactionService.findAllTransactions(uuid,filtres), uuid));

    }

    @GetMapping(value = "/{transactionUuid}")
    @IsUser
    public ResponseEntity<?> getTransaction(@PathVariable("uuid") String uuid,@PathVariable("transactionUuid") String transactionUuid){
        var t = transactionService.findTransaction(uuid,transactionUuid);
        if(t.isPresent())
            return ResponseEntity.ok(transactionAssembler.toModelWithAccount(t.get(), uuid));
        return ResponseEntity.notFound().build();

    }

    @PatchMapping(value = "/{transactionUuid}")
    @IsUser
    public ResponseEntity<?> patchTransaction(@PathVariable("uuid") String uuid,@PathVariable("transactionUuid") String transactionUuid, @RequestBody Map<Object, Object> fields){
        var a = transactionService.patchTransaction(uuid,transactionUuid,fields);
        return Optional.ofNullable(a).filter(Optional::isPresent)
                .map(card -> ResponseEntity.ok(transactionAssembler.toModelWithAccount(card.get(),uuid)))
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping
    @IsUser
    public ResponseEntity<?> createTransaction(@PathVariable("uuid") String uuid, @RequestBody @Valid NewTransaction transaction){
        var t = transactionService.create(transaction,uuid);
        if(t.isEmpty())
            return ResponseEntity.status(403).build();
        URI location = linkTo(TransactionsController.class,uuid).slash(t.get().getUuid()).toUri();
        return ResponseEntity.created(location).build();
    }
}
