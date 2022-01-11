package fr.miage.revolut.repositories;

import fr.miage.revolut.entities.PivotTransactionCard;
import fr.miage.revolut.entities.TransactionCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface TransactionCardRepository extends JpaRepository<PivotTransactionCard, TransactionCard> {
    List<PivotTransactionCard> findByTransactionCard_Card_CardNumberAndTransactionCard_Transaction_DateGreaterThanEqual(String cardNumber, OffsetDateTime date);
    //List<PivotTransactionCard> findByTransactionCard_Transaction_DateGreaterThanEqual(OffsetDateTime date);


    //List<PivotTransactionCard> findByTransactionCard_Card_CardNumberAndTransactionCard_Transaction_GreaterThanEqualsAfter(String cardNumber, OffsetDateTime date);






}
