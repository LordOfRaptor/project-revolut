package fr.miage.revolut.mapper;

import fr.miage.revolut.dto.create.NewAccount;
import fr.miage.revolut.dto.view.AccountView;
import fr.miage.revolut.entities.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountsMapper {

    AccountView toDto(Account acc);

    Account toEntity(NewAccount dto);
}
