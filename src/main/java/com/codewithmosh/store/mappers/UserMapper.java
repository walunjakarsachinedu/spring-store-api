package com.codewithmosh.store.mappers;

import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.entities.dtos.CreateUserRequest;
import com.codewithmosh.store.entities.dtos.UpdateUserRequest;
import com.codewithmosh.store.entities.dtos.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
  // here method name doesn't mapper, only input & return type matter
   UserDto toDto(User user);
   User toEntity(CreateUserRequest request);
   void update(UpdateUserRequest request, @MappingTarget User user);
}
