package emsi.projet.ebankingbackend.entities;

import emsi.projet.ebankingbackend.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccountOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private Date operationDate ;
    private double amount ;
    @Enumerated(EnumType.STRING)
    private OperationType type ;
    @ManyToOne
    private BankAccount bankAccount ;
    private String description ;
}
