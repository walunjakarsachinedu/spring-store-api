package com.codewithmosh.store.users;

import com.codewithmosh.store.users.dtos.CreateUserRequest;
import com.codewithmosh.store.users.dtos.UpdateUserRequest;
import com.codewithmosh.store.users.dtos.UserDto;
import com.codewithmosh.store.users.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
  // here method name doesn't mapper, only input & return type matter
   UserDto toDto(User user);
   User toEntity(CreateUserRequest request);
   void update(UpdateUserRequest request, @MappingTarget User user);
}
