package com.codewithmosh.store.orders;

public class OrderAccessDeniedException extends RuntimeException{
  public OrderAccessDeniedException() {
    super("user is not authorized to access order");
  }
}
