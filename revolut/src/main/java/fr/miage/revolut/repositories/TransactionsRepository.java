package fr.miage.revolut.repositories;


import fr.miage.revolut.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, String> {



    @Query(value = "SELECT t from Transaction t " +
            "WHERE (t.creditAccount=:iban or t.debtorAccount=:iban ) and " +
            "(:category is null OR t.category LIKE :category) and " +
            "(:label is null OR t.label LIKE :label) and " +
            "(:country is null OR t.country LIKE :country)")
    List<Transaction> findTransactionByIbanLabelCountryCategory(@Param("iban") String iban,@Param("category") String category,@Param("country") String country,@Param("label") String label);


    @Query("select t from Transaction t where t.uuid = ?1 and (t.creditAccount = ?2 or t.debtorAccount = ?2)")
    Optional<Transaction> findByUuidAndCreditAccountOrDebtorAccount(String uuid, String iban);

    Optional<Transaction> findByUuidAndDebtorAccount(String uuid, String debtorAccount);










}
