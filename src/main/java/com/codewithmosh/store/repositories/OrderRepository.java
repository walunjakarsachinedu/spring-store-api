package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
  @EntityGraph(attributePaths = "items.product")
  @Query("select o from Order o where o.customer.id = :customerId")
  List<Order> findByCustomer(@Param("customerId") Long customerId);
}
