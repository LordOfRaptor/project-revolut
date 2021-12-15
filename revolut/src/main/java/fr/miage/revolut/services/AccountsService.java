package fr.miage.revolut.services;

import fr.miage.revolut.dto.create.NewAccount;
import fr.miage.revolut.entities.Account;
import fr.miage.revolut.mapper.AccountsMapper;
import fr.miage.revolut.repositories.AccountsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ExposesResourceFor(Account.class)
public class AccountsService {

    private final AccountsRepository accRep;
    private final AccountsMapper mapper;

    public Optional<Account> findAccount(String uuid) {
        return accRep.findById(uuid);
    }

    public Account saveAccount(NewAccount acc) {
        Account a = mapper.toEntity(acc);
        a.setUuid(UUID.randomUUID().toString());
        //Pas un vrai IBAN voir https://github.com/arturmkrtchyan/iban4j
        a.setIban(a.getCountry().substring(0, 2).toUpperCase(Locale.ROOT)
                + UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase(Locale.ROOT));
        a = accRep.save(a);
        return a;
    }


}
