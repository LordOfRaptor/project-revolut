package fr.miage.revolut.repositories;

import fr.miage.revolut.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Account, String> {

    boolean existsByIban(String iban);

    Optional<Account> findByIban(String iban);





}
