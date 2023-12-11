package no.pgr209.machinefactory.Customer;

import no.pgr209.machinefactory.model.Address;
import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.repo.AddressRepo;
import no.pgr209.machinefactory.repo.CustomerRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("dev") // Seperate CommandLine and Data Jpa test.
public class CustomerRepoUnitTest {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Test
    public void save_shouldReturnCustomer() {
        Customer customer = new Customer();
        Customer savedCustomer = customerRepo.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getCustomerId()).isNotNull();
    }

    @Test
    public void save_shouldReturnSavedCustomerWithAddress() {
        Address addressOne = addressRepo.save(new Address("Kongens Gate 15", "Oslo", 1350));
        Address addressTwo = addressRepo.save(new Address("Bjarnes Gate 23", "Viken", 2150));
        List<Address> allAddresses = Arrays.asList(addressOne, addressTwo);

        Customer createCustomer = new Customer();
        createCustomer.setAddresses(allAddresses);
        Customer savedCustomer = customerRepo.save(createCustomer);

        Optional<Customer> findCustomer = customerRepo.findById(savedCustomer.getCustomerId());
        findCustomer.ifPresent(customer -> assertEquals(allAddresses, findCustomer.get().getAddresses()));
    }

    @Test
    public void findAll_shouldReturnNonEmptyListOfCustomers() {
        Customer firstCustomer = new Customer();
        Customer secondCustomer = new Customer();
        customerRepo.save(firstCustomer);
        customerRepo.save(secondCustomer);

        List<Customer> customers = customerRepo.findAll();

        assertThat(customers).isNotNull();
        assertThat(customers.size()).isGreaterThan(0);
    }

    @Test
    public void findAll_shouldReturnCustomer() {
        Customer customer = new Customer();
        Customer savedCustomer = customerRepo.save(customer);

        Optional<Customer> foundCustomer = customerRepo.findById(savedCustomer.getCustomerId());

        assertThat(foundCustomer).isPresent();
    }

    @Test
    public void findById_shouldNotReturnNonExistentCustomer() {
        Long nonExistentCustomer = 23413L;

        Optional<Customer> findCustomer = customerRepo.findById(nonExistentCustomer);

        assertThat(findCustomer).isNotPresent();
    }

    @Test // Create and then update customer
    public void update_shouldUpdateExistingCustomer() {

        // Create Customer with information
        Customer customer = customerRepo.save(new Customer("James Brown", "james@brown.no"));

        Optional<Customer> createdCustomer = customerRepo.findById(customer.getCustomerId());
        createdCustomer.ifPresent(customerMade -> assertEquals("James Brown", createdCustomer.get().getCustomerName()));

        // Update name
        customer.setCustomerName("Tom Hardy");
        customerRepo.save(customer);

        Optional<Customer> CustomerUpdated = customerRepo.findById(customer.getCustomerId());
        CustomerUpdated.ifPresent(customerMade -> assertEquals("Tom Hardy", CustomerUpdated.get().getCustomerName()));

    }
}
