package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
