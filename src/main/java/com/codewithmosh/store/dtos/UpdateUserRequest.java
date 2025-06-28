package com.codewithmosh.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class UpdateUserRequest {
  public String name;
  public String email;
}
