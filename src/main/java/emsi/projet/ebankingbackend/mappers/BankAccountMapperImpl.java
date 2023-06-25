package emsi.projet.ebankingbackend.mappers;

import emsi.projet.ebankingbackend.dtos.AccountOperationDTO;
import emsi.projet.ebankingbackend.dtos.CurrentBankAccountDTO;
import emsi.projet.ebankingbackend.dtos.CustomerDTO;
import emsi.projet.ebankingbackend.dtos.SavingBankAccountDTO;
import emsi.projet.ebankingbackend.entities.AccountOperation;
import emsi.projet.ebankingbackend.entities.CurrentAccount;
import emsi.projet.ebankingbackend.entities.Customer;
import emsi.projet.ebankingbackend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl{
    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO() ;
        BeanUtils.copyProperties(customer,customerDTO);
//        customerDTO.setName(customer.getName());
//        customerDTO.setId(customer.getId());
//        customerDTO.setEmail(customer.getEmail());
        return customerDTO ;
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO)
    {
        Customer customer = new Customer() ;
        BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }
    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount)
    {
        SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO() ;
        BeanUtils.copyProperties(savingAccount,savingBankAccountDTO);
        savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingBankAccountDTO ;
    }
    public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO)
    {
        SavingAccount savingAccount = new SavingAccount() ;
        BeanUtils.copyProperties(savingBankAccountDTO,savingAccount);
        savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
        return savingAccount ;
    }

    public CurrentBankAccountDTO fromCurrentAccount(CurrentAccount currentAccount)
    {
        CurrentBankAccountDTO currentBankAccountDTO = new CurrentBankAccountDTO() ;
        BeanUtils.copyProperties(currentAccount,currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
         currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentBankAccountDTO ;

    }
    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO)
    {
        CurrentAccount currentAccount = new CurrentAccount() ;
        BeanUtils.copyProperties(currentBankAccountDTO,currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
        return currentAccount ;
    }
    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation)
    {
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO() ;
        BeanUtils.copyProperties(accountOperation , accountOperationDTO);
        return accountOperationDTO ;
    }





}
