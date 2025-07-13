package com.codewithmosh.store.orders.dtos;

public class OrderNotFoundException extends RuntimeException {
  public OrderNotFoundException() {
    super("order not found.");
  }
}
