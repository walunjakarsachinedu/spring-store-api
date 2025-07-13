package com.codewithmosh.store.orders.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class OrderItemDto {
  private final Long id;
  private final OrderProductDto product;
  private final BigDecimal unitPrice;
  private final Integer quantity;
  private final BigDecimal totalPrice;
}
