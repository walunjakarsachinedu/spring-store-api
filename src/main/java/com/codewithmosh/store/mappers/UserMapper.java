package com.codewithmosh.store.mappers;

import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.entities.dtos.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  // here method name doesn't mapper, only input & return type matter
   UserDto toDto(User user);
}
