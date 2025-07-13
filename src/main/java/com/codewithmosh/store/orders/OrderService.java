package com.codewithmosh.store.orders;

import com.codewithmosh.store.auth.services.AuthService;
import com.codewithmosh.store.orders.dtos.OrderNotFoundException;
import com.codewithmosh.store.orders.entities.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final AuthService authService;

  public Order getOrder(Long orderId) {
    var order = orderRepository.getOrderWithItems(orderId).orElseThrow(OrderNotFoundException::new);

    if(!order.isOwnByUser(authService.getUser())) {
      throw new OrderAccessDeniedException();
    }

    return order;
  }

  public List<Order> getAllOrders() {
    var customerId = authService.getUser().getId();
    return orderRepository.getAllByCustomer(customerId);
  }
}
