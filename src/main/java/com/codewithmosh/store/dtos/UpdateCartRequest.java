package com.codewithmosh.store.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class UpdateCartRequest {
  @NotNull(message = "quantity is required")
  @Positive(message = "quantity must be positive")
  private Integer quantity;
}
