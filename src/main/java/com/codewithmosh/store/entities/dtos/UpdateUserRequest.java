package com.codewithmosh.store.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class UpdateUserRequest {
  public String name;
  public String email;
}
