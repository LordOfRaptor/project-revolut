package fr.miage.revolut.mapper;

import fr.miage.revolut.dto.create.NewTransaction;
import fr.miage.revolut.dto.view.TransactionView;
import fr.miage.revolut.entities.Transaction;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TransactionsMapper {

    @BeforeMapping
    default void beforeMapping(@MappingTarget TransactionView target, Transaction source,String iban) {
        if (source.getDebtorAccount().equals(iban) ){
            target.setAmount(source.getAmount() * -1);
        }
        else {
            target.setAmount(source.getAmount());
        }
    }

    @Mapping(target = "amount", ignore = true)
    TransactionView toDto(Transaction card);

    Transaction toEntity(NewTransaction dto);
}
