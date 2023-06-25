package emsi.projet.ebankingbackend.exceptions;

import emsi.projet.ebankingbackend.entities.BankAccount;

public class BankAccountNotFoundException extends Exception{
    public BankAccountNotFoundException(String message) {
        super(message);
    }
}
