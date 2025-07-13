package com.codewithmosh.store.orders;

import com.codewithmosh.store.orders.entities.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Long> {
  @EntityGraph(attributePaths = "items.product")
  @Query("select o from Order o where o.customer.id = :customerId")
  List<Order> getAllByCustomer(@Param("customerId") Long customerId);

  @EntityGraph(attributePaths = "items.product")
  @Query("select o from Order o where o.id = :orderId")
  Optional<Order> getOrderWithItems(@Param("orderId") Long orderId);
}
