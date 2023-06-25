package emsi.projet.ebankingbackend;

import emsi.projet.ebankingbackend.dtos.BankAccountDTO;
import emsi.projet.ebankingbackend.dtos.CurrentBankAccountDTO;
import emsi.projet.ebankingbackend.dtos.CustomerDTO;
import emsi.projet.ebankingbackend.dtos.SavingBankAccountDTO;
import emsi.projet.ebankingbackend.entities.*;
import emsi.projet.ebankingbackend.enums.AccountStatus;
import emsi.projet.ebankingbackend.enums.OperationType;
import emsi.projet.ebankingbackend.exceptions.BalanceNotSufficentException;
import emsi.projet.ebankingbackend.exceptions.BankAccountNotFoundException;
import emsi.projet.ebankingbackend.exceptions.CustomerNotFoundException;
import emsi.projet.ebankingbackend.repositories.AccountOperationRepository;
import emsi.projet.ebankingbackend.repositories.BankAccountRepository;
import emsi.projet.ebankingbackend.repositories.CustomerRepository;
import emsi.projet.ebankingbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}
    @Bean
	CommandLineRunner commandLineRunner (BankAccountService bankAccountService)
	{
		return args -> {
			Stream.of("Hassan" , "Imane" , "Mohamed")
					.forEach(name -> {
						CustomerDTO customer = new CustomerDTO();
						customer.setName(name);
						customer.setEmail(name + "@gmail.com");
						bankAccountService.saveCustomer(customer) ;
					});
			bankAccountService.listCustomers().forEach(customer -> {
				try {
					bankAccountService.saveCurrentBankAccount(Math.random()*90000 , customer.getId(), 9000 ) ;
				    bankAccountService.saveSavingBankAccount(Math.random()*120000, customer.getId(), 5.5) ;

				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}

			});
			List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList() ;
			for (BankAccountDTO bankAccount : bankAccounts) {
				String accountId ;
				if (bankAccount instanceof SavingBankAccountDTO)
				{
					accountId = ((SavingBankAccountDTO)bankAccount).getId() ;
				}
				else {
					accountId = ((CurrentBankAccountDTO)bankAccount).getId() ;

				}
				bankAccountService.credit(accountId ,10000+Math.random()*120000,"Credit");
				bankAccountService.debit(accountId,1000+Math.random()*9000, "Debit");
			}

		};
	}



	//@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository){
		return args -> {
			Stream.of("Hassan" , "Yassine" , "Aisha")
					.forEach(name->{
						Customer customer = new Customer() ;
						customer.setName(name);
						customer.setEmail(name+"@gamil");
						customerRepository.save(customer) ;
					});
			customerRepository.findAll().forEach(customer -> {
				CurrentAccount currentAccount = new CurrentAccount() ;
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*9000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(customer);
				currentAccount.setOverDraft(9000);
				bankAccountRepository.save(currentAccount) ;

				SavingAccount savingAccount = new SavingAccount() ;
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random()*9000);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setCustomer(customer);
				savingAccount.setInterestRate(5.5);
				bankAccountRepository.save(savingAccount) ;

			});
			bankAccountRepository.findAll().forEach(acc -> {
				for (int i = 0 ; i< 10  ;i++)
				{
					AccountOperation accountOperation = new AccountOperation() ;
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random()*12000);
					accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
					accountOperation.setBankAccount(acc);
					accountOperationRepository.save(accountOperation) ;
				}
			});
		};
	}


}
