package fr.miage.revolut.mapper;

import fr.miage.revolut.dto.create.NewTransaction;
import fr.miage.revolut.dto.view.TransactionView;
import fr.miage.revolut.entities.Transaction;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface TransactionsMapper {

    @BeforeMapping
    default void amountBigDecimalToLong(Transaction transaction, @MappingTarget TransactionView transactionView) {
        BigDecimal amount = new BigDecimal(transaction.getAmount());
        transactionView.setAmount(amount.divide(BigDecimal.valueOf(100)));
    }

    @BeforeMapping
    default void amountBigDecimalToLong(@MappingTarget Transaction transaction,  NewTransaction newTransaction) {
        BigDecimal bd = newTransaction.getAmount().multiply(BigDecimal.valueOf(100));
        transaction.setAmount(bd.toBigInteger().toString());
    }

    @Mapping(target = "amount", ignore = true)
    TransactionView toDto(Transaction card);

    @Mapping(target = "amount", ignore = true)
    Transaction toEntity(NewTransaction dto);
}
