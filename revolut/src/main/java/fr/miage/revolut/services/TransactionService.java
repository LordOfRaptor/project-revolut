package fr.miage.revolut.services;

import fr.miage.revolut.controllers.feignClients.ConversionClient;
import fr.miage.revolut.dto.create.NewTransaction;
import fr.miage.revolut.dto.patch.PatchTransaction;
import fr.miage.revolut.entities.Account;
import fr.miage.revolut.entities.Transaction;
import fr.miage.revolut.mapper.TransactionsMapper;
import fr.miage.revolut.repositories.AccountsRepository;
import fr.miage.revolut.repositories.TransactionsRepository;
import fr.miage.revolut.services.validator.PatchTransactionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@ExposesResourceFor(Transaction.class)
public class TransactionService {

    private final TransactionsRepository transactionsRepository;
    private final AccountsRepository accountsRepository;
    private final PatchTransactionValidator validator;
    private final TransactionsMapper mapper;
    private final ConversionClient conversionClient;

    public List<Transaction> findAllTransactions(String uuid, Map<String,String> filtres) {
        var acc = accountsRepository.findById(uuid);
        if(acc.isPresent()) {
            AtomicReference<String> label = new AtomicReference<>();
            AtomicReference<String> country = new AtomicReference<>();
            AtomicReference<String> category = new AtomicReference<>();
            filtres.forEach((f, v) -> {
                switch (f){
                    case "label" -> label.set(v);
                    case "country" -> country.set(v);
                    case "category" -> category.set(v);
                }
            });
            var transactions = transactionsRepository.findTransactionByIbanLabelCountryCategory(acc.get().getIban(), category.get(), country.get(), label.get());
            return listDebtorOrReceiver(transactions,acc.get().getIban());


        }
        return Collections.emptyList();

    }

    public Optional<Transaction> findTransaction(String uuid, String transactionUuid) {
        var acc = accountsRepository.findById(uuid);
        if(acc.isPresent()) {
            var transaction = transactionsRepository.findByUuidAndCreditAccountOrDebtorAccount(transactionUuid, acc.get().getIban());
            if(transaction.isPresent())
                return Optional.of(debtorOrReceiver(transaction.get(),acc.get().getIban()));

        }
        return Optional.empty();
    }

    @Transactional
    public Optional<Transaction> patchTransaction(String uuid, String transactionUuid, Map<Object, Object> fields) {
        var acc = accountsRepository.findById(uuid);
        if(acc.isPresent()) {
            var transaction = transactionsRepository.findByUuidAndDebtorAccount(transactionUuid, acc.get().getIban());
            if(transaction.isPresent()) {
                var transac = transaction.get();
                fields.forEach((f, v) -> {
                    switch (f.toString()) {
                        case "category" -> transac.setCategory((v != null) ? v.toString() : null);
                        case "label" -> transac.setLabel((v != null) ? v.toString() : null);
                        case "creditAccountName" -> transac.setCreditAcountName((v != null) ? v.toString() : null);
                        case "debtorAccountName" -> transac.setDebtorAccountName((v != null) ? v.toString() : null);
                    }
                });
                PatchTransaction pt = new PatchTransaction();
                pt.setCategory(transac.getCategory());
                pt.setLabel(transac.getLabel());
                pt.setCreditAccountName(transac.getCreditAcountName());
                pt.setDebtorAccountName(transac.getDebtorAccountName());
                validator.validate(pt);
                return Optional.of(transactionsRepository.save(transac));

            }

        }
        return Optional.empty();
    }

    @Transactional
    public Optional<Transaction> create(NewTransaction transaction,String uuid) {
        var a = accountsRepository.findById(uuid);
        if(a.isEmpty())
            return Optional.empty();
        var transac = mapper.toEntity(transaction);
        BigInteger transacAmount = new BigInteger(transac.getAmount());
        BigInteger accountAmount = new BigInteger(a.get().getSolde());
        if(transacAmount.compareTo(accountAmount) > 0)
            return Optional.empty();
        transac.setUuid(String.valueOf(UUID.randomUUID()));
        transac.setDate(OffsetDateTime.now());
        transac.setDebtorAccount(a.get().getIban());
        if(transaction.getCountry().equalsIgnoreCase(a.get().getCountry())){
            transac.setChangeRate(BigDecimal.ONE);
        }
        else {
            transac.setChangeRate(conversionClient.getConversion(a.get().getCountry(),transac.getCountry()));
        }
        if(accountsRepository.existsByIban(transaction.getCreditAccount())){
            var a2 = accountsRepository.findByIban(transaction.getCreditAccount());
            if(a2.isPresent()){
                Account acc = a2.get();
                BigInteger account2Amount = new BigInteger(acc.getSolde());
                BigDecimal bd = new BigDecimal(transacAmount);
                account2Amount = account2Amount.add(bd.multiply(transac.getChangeRate()).toBigInteger());
                acc.setSolde(account2Amount.toString());
                accountsRepository.save(acc);
            }
            /*
            Brancher sur une autre banque
             */
        }
        Account acc = a.get();
        accountAmount = accountAmount.add(transacAmount.negate());
        acc.setSolde(accountAmount.toString());
        accountsRepository.save(acc);

        transac = transactionsRepository.save(transac);
        return Optional.of(transac);
    }

    private Transaction debtorOrReceiver(Transaction t, String iban){
        if(t.getDebtorAccount().equals(iban)){
            t.setAmount("-"+t.getAmount());
        }
        else if(t.getCreditAccount().equals(iban)){
            BigDecimal bd = new BigDecimal(t.getAmount());
            t.setAmount(bd.multiply(t.getChangeRate()).toBigInteger().toString());
        }
        return t;
    }

    private List<Transaction> listDebtorOrReceiver(List<Transaction> transactions, String iban){
        return transactions.stream().map(t -> debtorOrReceiver(t,iban)).toList();

    }


}
