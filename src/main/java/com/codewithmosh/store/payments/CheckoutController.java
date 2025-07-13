package com.codewithmosh.store.payments;

import com.codewithmosh.store.common.ErrorDto;
import com.codewithmosh.store.carts.exceptions.CartEmptyException;
import com.codewithmosh.store.carts.exceptions.CartNotFoundException;
import com.codewithmosh.store.payments.dtos.CheckoutRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
  private final CheckoutService checkoutService;

  @PostMapping
  public ResponseEntity<?> checkout(@Valid @RequestBody CheckoutRequest request) {
      var checkoutResponse = checkoutService.checkout(request);
      return ResponseEntity.ok().body(checkoutResponse);
  }

  @PostMapping("/webhook")
  public void checkoutWebhook(
    @RequestHeader Map<String, String> headers,
    @RequestBody String payload
    ) {
    checkoutService.handleWebhookRequest(headers, payload);
  }

  @ExceptionHandler(PaymentException.class)
  public ResponseEntity<?> paymentExceptionHandler(Exception exp) {
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(new ErrorDto("Error in creating a checkout session"));
  }

  @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
  public ResponseEntity<?> cartNotFoundHandler(Exception exp) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
      new ErrorDto(exp.getMessage())
    );
  }
}