package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.CheckoutRequest;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckoutService {
  private CartRepository cartRepository;
  private AuthService authService;
  private OrderRepository orderRepository;

  public Order checkout(CheckoutRequest request) {
    var cart = cartRepository.findById(request.getCartId()).orElseThrow(CartNotFoundException::new);
    if(cart.isEmpty()) throw new CartEmptyException();

    // emptying cart
    cart.clearCart();
    cartRepository.save(cart);

    // creating order
    var order = Order.from(cart, authService.getUser());
    orderRepository.save(order);

    return order;
  }
}
