package emsi.projet.ebankingbackend.services;

import emsi.projet.ebankingbackend.dtos.*;
import emsi.projet.ebankingbackend.entities.BankAccount;
import emsi.projet.ebankingbackend.entities.CurrentAccount;
import emsi.projet.ebankingbackend.entities.Customer;
import emsi.projet.ebankingbackend.entities.SavingAccount;
import emsi.projet.ebankingbackend.exceptions.BalanceNotSufficentException;
import emsi.projet.ebankingbackend.exceptions.BankAccountNotFoundException;
import emsi.projet.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance  , Long customerId , double overdraft) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance  , Long customerId , double intrestRate) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomers() ;
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId , double amount , String description) throws BankAccountNotFoundException, BalanceNotSufficentException;
    void credit(String accountId , double amount , String description) throws BankAccountNotFoundException;
    void transefer(String accountIdSource , String accountIdDestination , double amount) throws BankAccountNotFoundException, BalanceNotSufficentException;

    List<BankAccountDTO> bankAccountList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
}
