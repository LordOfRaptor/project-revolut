package fr.miage.revolut.services;

import fr.miage.revolut.controllers.feignClients.ConversionClient;
import fr.miage.revolut.dto.PaymentOnline;
import fr.miage.revolut.dto.PaymentPhysique;
import fr.miage.revolut.entities.*;
import fr.miage.revolut.repositories.AccountsRepository;
import fr.miage.revolut.repositories.CardsRepository;
import fr.miage.revolut.repositories.TransactionCardRepository;
import fr.miage.revolut.repositories.TransactionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TransactionsRepository transactionsRepository;
    private final AccountsRepository accountsRepository;
    private final CardsRepository cardsRepository;
    private final TransactionCardRepository transactionCardRepository;
    private final ConversionClient conversionClient;


    public Optional<Transaction> createTransaction(PaymentOnline payment) {
        var c = cardsRepository.findByCardNumberAndCvvAndDeleteIsFalse(payment.getCardNumber(), payment.getCvv());
        if(c.isEmpty())
            return Optional.empty();
        Card card = c.get();

        Account a = card.getAccount();
        var pivotTransactionCards = transactionCardRepository.findByTransactionCard_Card_CardNumberAndTransactionCard_Transaction_DateGreaterThanEqual(card.getCardNumber(),OffsetDateTime.now().minusDays(30));
        var amount = pivotTransactionCards.stream().map(p -> new BigInteger(p.getTransactionCard().getTransaction().getAmount())).toList();
        var total = amount.stream().reduce(BigInteger.ZERO,BigInteger::add);
        if((card.getLocation() && !(a.getCountry().equalsIgnoreCase(payment.getCountry())) ||
             card.getBlocked() || (total.compareTo( (new BigInteger(String.valueOf(card.getLimit()))).multiply(BigInteger.valueOf(100)) ) > 0)))
            return Optional.empty();
        Transaction transaction = new Transaction();
        transaction.setUuid(UUID.randomUUID().toString());
        transaction.setAmount(payment.getAmount().multiply(BigDecimal.valueOf(100)).toBigInteger().toString());
        transaction.setCreditAccount(payment.getCreditAccount());
        transaction.setDebtorAccount(a.getIban());
        transaction.setDate(OffsetDateTime.now());
        transaction.setCountry(payment.getCountry());
        BigInteger transacAmount = new BigInteger(transaction.getAmount());
        BigInteger accountAmount = new BigInteger(a.getSolde());
        if(transacAmount.compareTo(accountAmount) > 0)
            return Optional.empty();
        if(transaction.getCountry().equalsIgnoreCase(a.getCountry())){
            transaction.setChangeRate(BigDecimal.ONE);
        }
        else {
            transaction.setChangeRate(conversionClient.getConversion(a.getCountry(),transaction.getCountry()).setScale(2, RoundingMode.HALF_DOWN));
        }
        if(accountsRepository.existsByIban(transaction.getCreditAccount())){
            var a2 = accountsRepository.findByIban(transaction.getCreditAccount());
            if(a2.isPresent()){
                Account acc = a2.get();
                BigInteger account2Amount = new BigInteger(acc.getSolde());
                BigDecimal bd = new BigDecimal(transaction.getAmount());
                account2Amount = account2Amount.add(bd.multiply(transaction.getChangeRate()).toBigInteger());
                acc.setSolde(account2Amount.toString());
                accountsRepository.save(acc);
            }
            /*
            Brancher sur une autre banque
             */
        }
        accountAmount = accountAmount.add(transacAmount.negate());
        a.setSolde(accountAmount.toString());
        var pivoTransactionCard = new PivotTransactionCard();
        var transactionCard = new TransactionCard();
        transactionCard.setTransaction(transaction);
        transactionCard.setCard(card);
        pivoTransactionCard.setTransactionCard(transactionCard);

        accountsRepository.save(a);
        var transac = transactionsRepository.save(transaction);
        transactionCardRepository.save(pivoTransactionCard);
        if(Boolean.TRUE.equals(card.getVirtual())){
            card.setDelete(true);
            cardsRepository.save(card);
        }
        return Optional.of(transac);
    }

    @Transactional
    public Optional<Transaction>  createTransaction(PaymentPhysique payment) {
        Optional<Card> c;
        if(payment.getContacless()) {
            c = cardsRepository.findByCardNumberAndContactlessIsTrue(payment.getCardNumber());
        }
        else {
            c = cardsRepository.findByCardNumberAndCodeAndDeleteIsFalse(payment.getCardNumber(), payment.getCode());
        }
        if(c.isEmpty())
            return Optional.empty();
        Card card = c.get();
        Account a = card.getAccount();
        var pivotTransactionCards = transactionCardRepository.findByTransactionCard_Card_CardNumberAndTransactionCard_Transaction_DateGreaterThanEqual(card.getCardNumber(),OffsetDateTime.now().minusDays(30));
        var amount = pivotTransactionCards.stream().map(p -> new BigInteger(p.getTransactionCard().getTransaction().getAmount())).toList();
        var total = amount.stream().reduce(BigInteger.ZERO,BigInteger::add);
        if((card.getLocation() && !(a.getCountry().equalsIgnoreCase(payment.getCountry())) ||
                card.getBlocked() || (total.compareTo( (new BigInteger(String.valueOf(card.getLimit()))).multiply(BigInteger.valueOf(100)) ) > 0)))
            return Optional.empty();
        Transaction transaction = new Transaction();
        transaction.setUuid(UUID.randomUUID().toString());
        transaction.setAmount(payment.getAmount().multiply(BigDecimal.valueOf(100)).toBigInteger().toString());
        transaction.setCreditAccount(payment.getCreditAccount());
        transaction.setDebtorAccount(a.getIban());
        transaction.setDate(OffsetDateTime.now());
        transaction.setCountry(payment.getCountry());
        BigInteger transacAmount = new BigInteger(transaction.getAmount());
        BigInteger accountAmount = new BigInteger(a.getSolde());
        if(transacAmount.compareTo(accountAmount) > 0)
            return Optional.empty();
        if(transaction.getCountry().equalsIgnoreCase(a.getCountry())){
            transaction.setChangeRate(BigDecimal.ONE);
        }
        else {
            transaction.setChangeRate(conversionClient.getConversion(a.getCountry(),transaction.getCountry()).setScale(2, RoundingMode.HALF_DOWN));
        }
        if(accountsRepository.existsByIban(transaction.getCreditAccount())){
            var a2 = accountsRepository.findByIban(transaction.getCreditAccount());
            if(a2.isPresent()){
                Account acc = a2.get();
                BigInteger account2Amount = new BigInteger(acc.getSolde());
                BigDecimal bd = new BigDecimal(transaction.getAmount());
                account2Amount = account2Amount.add(bd.multiply(transaction.getChangeRate()).toBigInteger());
                acc.setSolde(account2Amount.toString());
                accountsRepository.save(acc);
            }
            /*
            Brancher sur une autre banque
             */
        }
        accountAmount = accountAmount.add(transacAmount.negate());
        a.setSolde(accountAmount.toString());
        var pivoTransactionCard = new PivotTransactionCard();
        var transactionCard = new TransactionCard();
        transactionCard.setTransaction(transaction);
        transactionCard.setCard(card);
        pivoTransactionCard.setTransactionCard(transactionCard);

        accountsRepository.save(a);
        var transac = transactionsRepository.save(transaction);
        transactionCardRepository.save(pivoTransactionCard);
        if(Boolean.TRUE.equals(card.getVirtual())){
            card.setDelete(true);
            cardsRepository.save(card);
        }
        return Optional.of(transac);
    }
}
