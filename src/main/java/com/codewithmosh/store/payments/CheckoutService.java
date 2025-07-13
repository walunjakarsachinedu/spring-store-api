package com.codewithmosh.store.payments;

import com.codewithmosh.store.orders.entities.Order;
import com.codewithmosh.store.orders.entities.OrderStatus;
import com.codewithmosh.store.carts.exceptions.CartEmptyException;
import com.codewithmosh.store.carts.exceptions.CartNotFoundException;
import com.codewithmosh.store.carts.CartRepository;
import com.codewithmosh.store.orders.OrderRepository;
import com.codewithmosh.store.auth.services.AuthService;
import com.codewithmosh.store.carts.CartService;
import com.codewithmosh.store.payments.dtos.CheckoutRequest;
import com.codewithmosh.store.payments.dtos.CheckoutResponse;
import com.codewithmosh.store.payments.dtos.WebhookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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


  public void handleWebhookRequest(Map<String, String> headers, String payload) {
    paymentGateway.parseWebhookResponse(new WebhookRequest(headers, payload))
      .ifPresent((response) -> {
        var order = orderRepository.findById(response.orderId()).orElseThrow();
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
      });
  }
}
