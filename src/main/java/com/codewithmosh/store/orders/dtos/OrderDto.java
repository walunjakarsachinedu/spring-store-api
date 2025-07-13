package com.codewithmosh.store.orders.dtos;

import com.codewithmosh.store.orders.entities.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@Getter
public class OrderDto {
  private final Long id;
  private final OrderStatus status;
  private LocalDateTime createdAt;
  private final BigDecimal totalPrice;
  private final Set<OrderItemDto> items;
}