package com.codewithmosh.store.entities.dtos;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class CartItemDto {
  private Long id;
  private ProductDto product;
  private Integer quantity;
  private BigDecimal totalPrice = BigDecimal.ZERO;
}
