package fr.miage.revolut.services;

import fr.miage.revolut.entities.Transaction;
import fr.miage.revolut.repositories.AccountsRepository;
import fr.miage.revolut.repositories.TransactionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@ExposesResourceFor(Transaction.class)
public class TransactionService {

    private final TransactionsRepository transactionsRepository;
    private final AccountsRepository accountsRepository;

    public List<Transaction> findAllTransactions(String uuid, Map<String,String> filtres) {
        var acc = accountsRepository.findById(uuid);
        if(acc.isPresent()) {
            AtomicReference<String> label = new AtomicReference<>();
            AtomicReference<String> country = new AtomicReference<>();
            AtomicReference<String> category = new AtomicReference<>();
            filtres.forEach((f, v) -> {
                switch (f.toString()){
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

    private Transaction debtorOrReceiver(Transaction t, String iban){
        if(t.getDebtorAccount().equals(iban)){
            t.setAmount(t.getAmount()*-1);
        }
        return t;
    }

    private List<Transaction> listDebtorOrReceiver(List<Transaction> transactions, String iban){
        return transactions.stream().map(t -> debtorOrReceiver(t,iban)).toList();

    }
}
