package com.codewithmosh.store.entities.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class RegisterUserRequest {
  private String name;
  private String email;
  private String password;
}
