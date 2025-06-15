package com.codewithmosh.store.controllers;

import com.codewithmosh.store.entities.UserDto;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
  UserRepository userRepository;
  UserMapper userMapper;

  @GetMapping("/")
  public Iterable<UserDto> getAllUsers() {
    return userRepository.findAll()
      .stream()
      .map(userMapper::toDto)
      .toList();
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
    var user = userRepository.findById(id).orElse(null);
    if(user == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(userMapper.toDto(user));
  }
}
