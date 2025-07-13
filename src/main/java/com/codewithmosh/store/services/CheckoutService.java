package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.CheckoutRequest;
import com.codewithmosh.store.dtos.CheckoutResponse;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckoutService {
  private final CartRepository cartRepository;
  private final AuthService authService;
  private final OrderRepository orderRepository;
  private final CartService cartService;
  private final PaymentGateway paymentGateway;

  @Transactional
  public CheckoutResponse checkout(CheckoutRequest request) throws PaymentException {
    var cart = cartRepository.findById(request.getCartId()).orElseThrow(CartNotFoundException::new);
    if (cart.isEmpty()) throw new CartEmptyException();

    // creating order
    var order = Order.from(cart, authService.getUser());
    System.out.println("saving order containing " + order.getItems().size() + " items");
    orderRepository.save(order);

    try {
      // creating checkout session
      var session = paymentGateway.getCheckoutSession(order);

      // emptying cart
      cartService.clearCart(cart.getId().toString());

      return new CheckoutResponse(order.getId(), session.checkoutUrl());
    }
    catch (PaymentException ex) {
      orderRepository.delete(order);
      throw ex;
    }
  }
}
