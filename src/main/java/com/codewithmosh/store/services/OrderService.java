package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.exceptions.OrderAccessDeniedException;
import com.codewithmosh.store.exceptions.OrderNotFoundException;
import com.codewithmosh.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final AuthService authService;

  public Order getOrder(Long orderId) {
    var order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

    if(!order.isOwnByUser(authService.getUser())) {
      throw new OrderAccessDeniedException();
    }

    return order;
  }

  public List<Order> getAllOrders() {
    var customerId = authService.getUser().getId();
    return orderRepository.findByCustomer(customerId);
  }
}
