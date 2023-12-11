package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Address;
import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.model.CustomerDTO;
import no.pgr209.machinefactory.repo.AddressRepo;
import no.pgr209.machinefactory.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepo customerRepo;

    private final AddressRepo addressRepo;

    @Autowired
    public CustomerService(CustomerRepo customerRepo, AddressRepo addressRepo) {
        this.customerRepo = customerRepo;
        this.addressRepo = addressRepo;
    }

    //Get ALL customers
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    //Get customers by page
    public List<Customer> getCustomersByPage(int pageNr) {
        return customerRepo.findAll(PageRequest.of(pageNr, 10)).stream().toList();
    }

    public Customer getCustomerById(Long id) {
        return customerRepo.findById(id).orElse(null);
    }

    public Customer createCustomer(CustomerDTO customerDTO) {
        Customer newCustomer = new Customer();

        if(customerDTO.getCustomerName() == null){
            return null;
        }
        newCustomer.setCustomerName(customerDTO.getCustomerName());

        if(customerDTO.getCustomerEmail() == null){
            return null;
        }
        newCustomer.setCustomerEmail(customerDTO.getCustomerEmail());

        List<Long> addressIds = customerDTO.getAddressId();
        if(!addressIds.stream().allMatch(addressRepo::existsById)) {
            return null;
        }
        newCustomer.setAddresses(addressRepo.findAllById(addressIds));

        return customerRepo.save(newCustomer);
    }

    public void deleteCustomerById(Long id) {
        customerRepo.deleteById(id);
    }

    public boolean customerExists(Long id) {
        return customerRepo.existsById(id);
    }

    public Customer updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepo.findById(id).orElse(null);

        if(existingCustomer != null) {

            if(customerDTO.getCustomerName() != null) {
                existingCustomer.setCustomerName(customerDTO.getCustomerName());
            }

            if(customerDTO.getCustomerEmail() != null) {
                existingCustomer.setCustomerEmail(customerDTO.getCustomerEmail());
            }

            if(customerDTO.getAddressId() != null) {
                if(!addressRepo.findAllById(customerDTO.getAddressId()).isEmpty()) {
                    List<Address> addresses = addressRepo.findAllById(customerDTO.getAddressId());
                    existingCustomer.setAddresses(addresses);
                } else {
                    return null;
                }
            }

            return customerRepo.save(existingCustomer);
        } else {
            return null;
        }
    }
}
