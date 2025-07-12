package com.codewithmosh.store.dtos;

import com.codewithmosh.store.entities.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;

@AllArgsConstructor
@Getter
public class OrderDto {
  private final Long id;
  private final OrderStatus status;
  private final BigDecimal totalPrice;
  private final Set<OrderItemDto> items;
}