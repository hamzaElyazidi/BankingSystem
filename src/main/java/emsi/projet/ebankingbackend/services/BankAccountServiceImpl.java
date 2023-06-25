package emsi.projet.ebankingbackend.services;

import emsi.projet.ebankingbackend.dtos.*;
import emsi.projet.ebankingbackend.entities.*;
import emsi.projet.ebankingbackend.enums.OperationType;
import emsi.projet.ebankingbackend.exceptions.CustomerNotFoundException;
import emsi.projet.ebankingbackend.exceptions.BankAccountNotFoundException;
import emsi.projet.ebankingbackend.exceptions.BalanceNotSufficentException;

import emsi.projet.ebankingbackend.mappers.BankAccountMapperImpl;
import emsi.projet.ebankingbackend.repositories.AccountOperationRepository;
import emsi.projet.ebankingbackend.repositories.BankAccountRepository;
import emsi.projet.ebankingbackend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{
    private CustomerRepository customerRepository ;
    private BankAccountRepository bankAccountRepository ;
    private AccountOperationRepository accountOperationRepository ;
     private BankAccountMapperImpl dtoMapper ;


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO)
    {
        log.info("Saving a new Customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO) ;
        Customer savedCustomer = customerRepository.save(customer) ;
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, Long customerId, double overdraft) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null) ;
        if (customer == null)
            throw  new CustomerNotFoundException("Customer not found") ;
        CurrentAccount currentAccount  = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overdraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount) ;
        return dtoMapper.fromCurrentAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, Long customerId, double intrestRate) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null) ;
        if (customer == null)
            throw  new CustomerNotFoundException("Customer not found") ;
        SavingAccount savingAccount  = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(intrestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount) ;
        return dtoMapper.fromSavingBankAccount(savedBankAccount);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList()) ;
        return customerDTOS ;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount  bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("Bank Account not Found")) ;
        if (bankAccount instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) bankAccount ;
            return dtoMapper.fromSavingBankAccount(savingAccount) ;
        }
        else
        {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount ;
            return dtoMapper.fromCurrentAccount(currentAccount) ;
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException {
          BankAccount bankAccount =  bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("Bank Account not Found")) ;

        if (bankAccount.getBalance()< amount)
               throw new BalanceNotSufficentException("Balance not sufficent") ;

        AccountOperation accountOperation = new AccountOperation() ;
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation) ;
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount) ;
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount  bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("Bank Account not Found")) ;
        AccountOperation accountOperation = new AccountOperation() ;
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation) ;
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount) ;
    }

    @Override
    public void transefer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficentException {
        debit(accountIdSource,amount,"Transfer to " + accountIdDestination);
        credit(accountIdDestination,amount,"Transfert from " + accountIdSource);

    }
    @Override
    // MAY CAUSE PROBLEMES !!!
    public List<BankAccountDTO> bankAccountList(){

       return bankAccountRepository.findAll().stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount ;
                return dtoMapper.fromSavingBankAccount(savingAccount) ;
            }
            else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount ;
                return dtoMapper.fromCurrentAccount(currentAccount);
             }
        }).collect(Collectors.toList()) ;
    }
   @Override
   public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException
   {
       Customer customer = customerRepository.findById(customerId).orElseThrow(()->new CustomerNotFoundException("Customer not found")) ;
       return dtoMapper.fromCustomer(customer) ;
   }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO)
    {
        log.info("Saving a new Customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO) ;
        Customer savedCustomer = customerRepository.save(customer) ;
        return dtoMapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deleteCustomer(Long customerId)
    {
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId)
    {
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccount_Id(accountId) ;
      return   accountOperations.stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList()) ;
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null) ;
        if (bankAccount == null) throw new BankAccountNotFoundException("Account not found") ;
        Page<AccountOperation> accountOperations =   accountOperationRepository.findByBankAccount_Id(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO() ;
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList()) ;
        accountHistoryDTO.setAccountOperationDTOs(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        accountHistoryDTO.setCurrentPage(page);
        return accountHistoryDTO;
    }

}
