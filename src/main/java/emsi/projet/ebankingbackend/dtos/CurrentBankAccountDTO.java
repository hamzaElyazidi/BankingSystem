package emsi.projet.ebankingbackend.dtos;

import emsi.projet.ebankingbackend.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

;


@Data @NoArgsConstructor @AllArgsConstructor
public  class CurrentBankAccountDTO extends BankAccountDTO{

    private String id;
    private double balance ;
    private Date createdAt ;
    private AccountStatus status ;
    private CustomerDTO customerDTO ;
    private double overDraft ;
}
