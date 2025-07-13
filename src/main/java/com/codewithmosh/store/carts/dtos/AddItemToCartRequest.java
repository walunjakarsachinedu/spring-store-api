package com.codewithmosh.store.carts.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddItemToCartRequest {
  @NotNull(message = "productId is required")
  @Positive(message = "productId must be positive")
  private Long productId;
}
