package com.codewithmosh.store.controllers;

import com.codewithmosh.store.entities.dtos.RegisterUserRequest;
import com.codewithmosh.store.entities.dtos.UpdateUserRequest;
import com.codewithmosh.store.entities.dtos.UserDto;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
  UserRepository userRepository;
  UserMapper userMapper;

  @GetMapping
  public ResponseEntity<?> getAllUsers(
    @RequestHeader(value = "x-auth-token", required = false) String token,
    @RequestParam(value = "sort", required = false, defaultValue = "id") String sort
  ) {
    System.out.println("getAllUsers(x-auth-token: " + token + ")");
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
  public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
    var user = userRepository.findById(id).orElse(null);
    if(user == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(userMapper.toDto(user));
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<UserDto> addUser(
    @RequestBody RegisterUserRequest request,
    UriComponentsBuilder uriBuilder
  ) {
    var user = userMapper.toEntity(request);
    userRepository.save(user);

    var userDto = userMapper.toDto(user);
    var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
    return ResponseEntity.created(uri).body(userDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDto> updateUser(
    @PathVariable("id") Long id,
    @RequestBody UpdateUserRequest request
    ) {
    var user = userRepository.findById(id).orElse(null);
    if(user == null) return ResponseEntity.notFound().build();

    userMapper.update(request, user);
    userRepository.save(user);

    return ResponseEntity.ok(userMapper.toDto(user));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
    var user = userRepository.findById(id).orElse(null);
    if(user == null) return ResponseEntity.notFound().build();

    userRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
