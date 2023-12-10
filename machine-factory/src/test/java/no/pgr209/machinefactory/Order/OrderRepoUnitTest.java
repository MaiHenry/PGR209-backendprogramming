package no.pgr209.machinefactory.Order;

import no.pgr209.machinefactory.model.Order;
import no.pgr209.machinefactory.repo.OrderRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("dev") // Seperate CommandLine and Data Jpa test.
public class OrderRepoUnitTest {

    @Autowired
    private OrderRepo orderRepo;

    @Test
    public void save_shouldReturnSavedOrder() {
        Order order = new Order();
        Order savedOrder = orderRepo.save(order);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getOrderId()).isNotNull();
    }

    @Test
    public void findAll_shouldReturnNonEmptyList() {
        Order firstOrder = new Order();
        Order secondOrder = new Order();
        orderRepo.save(firstOrder);
        orderRepo.save(secondOrder);

        List<Order> orders = orderRepo.findAll();

        assertThat(orders).isNotNull();
        assertThat(orders.size()).isGreaterThan(0);
    }

    @Test
    public void findById_shouldReturnOrder() {
        Order order = new Order();
        Order savedOrder = orderRepo.save(order);

        Optional<Order> foundOrder = orderRepo.findById(savedOrder.getOrderId());

        assertThat(foundOrder).isPresent();
    }

    @Test
    public void findById_shouldNotReturnNonExistentOrder() {
        Long nonExistentId = 65561L;

        Optional<Order> foundOrder = orderRepo.findById(nonExistentId);

        assertThat(foundOrder).isNotPresent();
    }

    @Test
    public void deleteById_shouldRemoveOrder() {
        Order order = new Order();
        Order savedOrder = orderRepo.save(order);
        Optional<Order> foundOrder = orderRepo.findById(savedOrder.getOrderId());

        assertThat(foundOrder).isPresent();

        orderRepo.deleteById(savedOrder.getOrderId());
        Optional<Order> deletedOrder = orderRepo.findById(savedOrder.getOrderId());

        assertThat(deletedOrder).isNotPresent();
    }
}
