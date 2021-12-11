package fr.miage.revolut.mapper;

import fr.miage.revolut.dto.create.NewTransaction;
import fr.miage.revolut.dto.view.TransactionView;
import fr.miage.revolut.entities.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionsMapper {

    TransactionView toDto(Transaction card);

    Transaction toEntity(NewTransaction dto);
}
