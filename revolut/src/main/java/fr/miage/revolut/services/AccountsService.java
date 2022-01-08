package fr.miage.revolut.services;

import fr.miage.revolut.dto.UserRequest;
import fr.miage.revolut.dto.create.NewAccount;
import fr.miage.revolut.dto.patch.PatchAccount;
import fr.miage.revolut.dto.view.AccountView;
import fr.miage.revolut.entities.Account;
import fr.miage.revolut.mapper.AccountsMapper;
import fr.miage.revolut.repositories.AccountsRepository;
import fr.miage.revolut.services.security.KeycloakService;
import fr.miage.revolut.services.validator.NewAccountValidator;
import fr.miage.revolut.services.validator.PacthAccountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ExposesResourceFor(Account.class)
public class AccountsService {

    private final AccountsRepository accRep;
    private final AccountsMapper mapper;
    private final KeycloakService keycloakService;
    private final PacthAccountValidator validator;

    public Optional<Account> findAccount(String uuid) {
        return accRep.findById(uuid);
    }

    public Account saveAccount(NewAccount acc) {
        Account a = mapper.toEntity(acc);
        String id = UUID.randomUUID().toString();
        UserRequest userRequest = new UserRequest();

        userRequest.setUsername(id);
        userRequest.setPassword(acc.getPassword());
        keycloakService.create(userRequest);
        a.setUuid(id);
        //Pas un vrai IBAN voir https://github.com/arturmkrtchyan/iban4j
        a.setIban(a.getCountry().substring(0, 2).toUpperCase(Locale.ROOT)
                + UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase(Locale.ROOT));
        a = accRep.save(a);
        return a;
    }


    public Optional<Account> patchAccount(String uuid, Map<Object, Object> fields) {
        var account = accRep.findById(uuid);
        if (account.isPresent()) {
            Account acc = account.get();

            fields.forEach((f, v) -> {
                switch (f.toString()){
                    case "passport" -> acc.setPassport(v.toString());
                    case "phoneNumber" -> acc.setPhoneNumber(v.toString());
                }
            });
            PatchAccount pa = new PatchAccount();
            pa.setPhoneNumber(acc.getPhoneNumber());
            pa.setPassport(acc.getPassport());
            validator.validate(pa);
            accRep.save(acc);
            return Optional.ofNullable(acc);
        }
        return Optional.empty();
    }
}
