package com.codewithmosh.store.controllers;

import com.codewithmosh.store.entities.dtos.UserDto;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
  UserRepository userRepository;
  UserMapper userMapper;

  @GetMapping
  public ResponseEntity<?> getAllUsers(
    @RequestParam(value = "sort", required = false, defaultValue = "id") String sort
  ) {
    if(!Set.of("id", "name", "email").contains(sort)) {
      return ResponseEntity.badRequest().body("Invalid sort by value, provide one of id/name/email");
    }
    return ResponseEntity.ok(
      userRepository.findAll(Sort.by(sort).ascending())
      .stream()
      .map(userMapper::toDto)
      .toList()
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUser(@PathVariable("user_id") Long id) {
    var user = userRepository.findById(id).orElse(null);
    if(user == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(userMapper.toDto(user));
  }
}
