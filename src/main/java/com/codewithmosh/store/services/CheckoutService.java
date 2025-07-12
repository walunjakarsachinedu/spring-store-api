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
  private CartService cartService;

  public Order checkout(CheckoutRequest request) {
    var cart = cartRepository.findById(request.getCartId()).orElseThrow(CartNotFoundException::new);
    if(cart.isEmpty()) throw new CartEmptyException();

    // creating order
    var order = Order.from(cart, authService.getUser());
    System.out.println("saving order containing " + order.getItems().size() + " items");
    orderRepository.save(order);

    // emptying cart
    cartService.clearCart(cart.getId().toString());

    return order;
  }
}
