package com.codewithmosh.store.carts.exceptions;

public class CartEmptyException extends RuntimeException {
  public CartEmptyException() {
    super("Cart is empty");
  }
}
