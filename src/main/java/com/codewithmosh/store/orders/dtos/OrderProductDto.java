package com.codewithmosh.store.orders.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderProductDto {
  private Long id;
  private String name;
  private String description;
  private Byte categoryId;
}
