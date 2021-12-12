package fr.miage.revolut.services;

import fr.miage.revolut.controllers.AccountsController;
import fr.miage.revolut.dto.create.NewAccount;
import fr.miage.revolut.dto.view.AccountView;
import fr.miage.revolut.entities.Account;
import fr.miage.revolut.mapper.AccountsMapper;
import fr.miage.revolut.repositories.AccountsRepository;
import fr.miage.revolut.services.assembler.AccountAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
@ExposesResourceFor(Account.class)
public class AccountsService {

    private final AccountsRepository accRep;
    private final AccountAssembler assembler;
    private final AccountsMapper mapper;

    public EntityModel<AccountView> findAccount(String uuid){
        return Optional.ofNullable(accRep.findById(uuid)).filter(Optional::isPresent)
                .map(account -> assembler.toModel(account.get()))
                .orElse(null);
    }

    public URI saveAccount(NewAccount acc) {
        Account a = mapper.toEntity(acc);
        a.setUuid(UUID.randomUUID().toString());
        //Pas un vrai IBAN voir https://github.com/arturmkrtchyan/iban4j
        a.setIban(a.getCountry().substring(0,2).toUpperCase(Locale.ROOT)
                + UUID.randomUUID().toString().replace("-","").substring(0,20).toUpperCase(Locale.ROOT));
        a = accRep.save(a);
        return linkTo(AccountsController.class).slash(a.getUuid()).toUri();
    }




}
