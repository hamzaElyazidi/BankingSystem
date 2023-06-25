package emsi.projet.ebankingbackend.repositories;

import emsi.projet.ebankingbackend.entities.BankAccount;
import emsi.projet.ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}
