package emsi.projet.ebankingbackend.web;

import emsi.projet.ebankingbackend.dtos.CustomerDTO;
import emsi.projet.ebankingbackend.entities.Customer;
import emsi.projet.ebankingbackend.exceptions.CustomerNotFoundException;
import emsi.projet.ebankingbackend.repositories.CustomerRepository;
import emsi.projet.ebankingbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/customers")
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
  private BankAccountService bankAccountService ;
  @GetMapping("/customers")
  public List<CustomerDTO> customers (){
      return bankAccountService.listCustomers() ;
  }
  @GetMapping("/customers/{id}")
  public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
    return bankAccountService.getCustomer(customerId) ;
  }
  @PostMapping("/customers")
  public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO)
  {
    return bankAccountService.saveCustomer(customerDTO) ;
  }
  @PutMapping("/customers/{customerId}")
  public CustomerDTO updateCustomer(@PathVariable Long customerId ,@RequestBody CustomerDTO customerDTO)
  {
    customerDTO.setId(customerId);
    return bankAccountService.updateCustomer(customerDTO) ;
  }
  @DeleteMapping("/customers/{id}")
  public void deleteCustomer(@PathVariable Long id)
  {
    bankAccountService.deleteCustomer(id);
  }
}
