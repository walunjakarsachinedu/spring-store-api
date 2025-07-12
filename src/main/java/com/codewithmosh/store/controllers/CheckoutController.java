package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.CheckoutRequest;
import com.codewithmosh.store.dtos.CheckoutResponse;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.services.CheckoutService;
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
  public ResponseEntity<CheckoutResponse> checkout(@Valid @RequestBody CheckoutRequest request) {
    var order = checkoutService.checkout(request);
    return ResponseEntity.ok().body(new CheckoutResponse(order.getId()));
  }

  @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
  public ResponseEntity<?> cartNotFoundHandler(Exception exp) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", exp.getMessage()));
  }
}
