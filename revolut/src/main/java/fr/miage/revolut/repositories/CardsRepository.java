package fr.miage.revolut.repositories;

import fr.miage.revolut.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardsRepository  extends JpaRepository<Card, String> {


    List<Card> findByAccount_UuidAndDeleteFalse(String uuid);


    Optional<Card> findByCardNumberAndAccount_UuidAndDeleteIsFalse(String cardNumber, String uuid);

    Optional<Card> findByCardNumberAndCodeAndDeleteIsFalse(String cardNumber, String code);

    Optional<Card> findByCardNumberAndContactlessIsTrue(String cardNumber);


    Optional<Card> findByCardNumberAndCvvAndDeleteIsFalse(String cardNumber, String cvv);




    boolean existsByCardNumber(String cardNumber);



















}
