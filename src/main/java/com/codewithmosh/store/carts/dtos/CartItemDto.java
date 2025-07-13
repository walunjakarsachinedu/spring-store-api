package com.codewithmosh.store.carts.dtos;

import com.codewithmosh.store.products.dtos.ProductDto;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class CartItemDto {
  private Long id;
  private ProductDto product;
  private Integer quantity;
  private BigDecimal totalPrice = BigDecimal.ZERO;
}
