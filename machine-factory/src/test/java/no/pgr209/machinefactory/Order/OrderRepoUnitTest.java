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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test") // Seperate CommandLine and Testing.
public class OrderRepoUnitTest {

    @Autowired
    private OrderRepo orderRepo;

    @Test
    public void OrderRepo_Save_ReturnSavedOrder() {
        Order order = new Order();
        Order savedOrder = orderRepo.save(order);

        assertNotNull(savedOrder);
        assertNotNull(savedOrder.getOrderId());
    }

    @Test
    public void OrderRepo_GetAll_ReturnNotEmpty() {
        Order firstOrder = new Order();
        Order secondOrder = new Order();
        orderRepo.save(firstOrder);
        orderRepo.save(secondOrder);

        List<Order> orders = orderRepo.findAll();

        assertNotNull(orders);
        assertThat(orders.size()).isGreaterThan(0);

    }

    @Test
    public void OrderRepo_FindById_ReturnOrder() {
        Order order = new Order();
        orderRepo.save(order);

        Optional<Order> foundOrder = orderRepo.findById(order.getOrderId());

        assertThat(foundOrder).isPresent();
    }
}
