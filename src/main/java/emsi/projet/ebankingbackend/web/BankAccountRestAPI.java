package emsi.projet.ebankingbackend.web;

import emsi.projet.ebankingbackend.dtos.AccountHistoryDTO;
import emsi.projet.ebankingbackend.dtos.AccountOperationDTO;
import emsi.projet.ebankingbackend.dtos.BankAccountDTO;
import emsi.projet.ebankingbackend.entities.AccountOperation;
import emsi.projet.ebankingbackend.entities.BankAccount;
import emsi.projet.ebankingbackend.exceptions.BankAccountNotFoundException;
import emsi.projet.ebankingbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class BankAccountRestAPI {
    private BankAccountService bankAccountService ;

    public BankAccountRestAPI(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }
    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId) ;
    }
    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts()
    {
        return bankAccountService.bankAccountList() ;
    }
    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId)
    {
        return bankAccountService.accountHistory(accountId) ;
    }
    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(@PathVariable String accountId,
                                               @RequestParam(name = "page" , defaultValue = "0") int page ,
                                               @RequestParam(name = "size" , defaultValue = "5")  int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page , size) ;
    }



}
