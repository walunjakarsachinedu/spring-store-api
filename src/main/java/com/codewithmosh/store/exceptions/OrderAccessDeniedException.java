package com.codewithmosh.store.exceptions;

public class OrderAccessDeniedException extends RuntimeException{
  public OrderAccessDeniedException() {
    super("user is not authorized to access order");
  }
}
