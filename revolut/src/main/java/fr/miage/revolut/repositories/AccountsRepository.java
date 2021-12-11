package fr.miage.revolut.repositories;

import fr.miage.revolut.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends JpaRepository<Account,String> {
}
