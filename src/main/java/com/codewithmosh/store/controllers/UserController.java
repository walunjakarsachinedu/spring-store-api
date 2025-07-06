package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ChangePasswordRequest;
import com.codewithmosh.store.dtos.CreateUserRequest;
import com.codewithmosh.store.dtos.UpdateUserRequest;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;


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
  public ResponseEntity<?> addUser(
    @Valid @RequestBody CreateUserRequest request,
    UriComponentsBuilder uriBuilder
  ) {
    // check user with email exists or not
    if(userRepository.existsByEmail(request.getEmail())) {
      return ResponseEntity.badRequest().body(
        Map.of("email", "Email is already registered.")
      );
    }

    var user = userMapper.toEntity(request);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
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

  @PostMapping("/{id}/change-password")
  public ResponseEntity<Void> changePassword(
    @PathVariable("id") Long id,
    @RequestBody ChangePasswordRequest request
  ) {
    var user = userRepository.findById(id).orElse(null);
    if(user == null) return ResponseEntity.notFound().build();

    if(!user.getPassword().equals(request.getOldPassword())) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    user.setPassword(request.getNewPassword());
    userRepository.save(user);

    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationErrors(
    MethodArgumentNotValidException exception
  ) {
    var errors = new HashMap<String, String>();
    exception.getBindingResult().getFieldErrors().forEach(
      error -> errors.put(error.getField(), error.getDefaultMessage())
    );
    return ResponseEntity.badRequest().body(errors);
  }
}
