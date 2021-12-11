package fr.miage.revolut.services.assembler;

import fr.miage.revolut.controllers.AccountsController;
import fr.miage.revolut.dto.view.AccountView;
import fr.miage.revolut.entities.Account;
import fr.miage.revolut.mapper.AccountsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class AccountAssembler implements RepresentationModelAssembler<Account, EntityModel<AccountView>> {

    private final AccountsMapper mapper;

    @Override
    public EntityModel<AccountView> toModel(Account acc) {
        return EntityModel.of(mapper.toDto(acc),
                linkTo(methodOn(AccountsController.class)
                        .getOneAccount(acc.getUuid())).withSelfRel(),
                linkTo(methodOn(AccountsController.class)
                        .getAccountCards(acc.getUuid())).withRel("cards"));
    }
}
