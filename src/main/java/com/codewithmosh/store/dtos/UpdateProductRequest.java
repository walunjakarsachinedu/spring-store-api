package com.codewithmosh.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;


@AllArgsConstructor
@Getter
public class UpdateProductRequest {
  private String name;
  private String description;
  private BigDecimal price;
  private Byte categoryId;
}
