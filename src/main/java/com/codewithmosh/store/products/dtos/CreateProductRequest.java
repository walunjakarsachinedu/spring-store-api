package com.codewithmosh.store.products.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class CreateProductRequest {
  private String name;
  private String description;
  private BigDecimal price;
  private Byte categoryId;
}
