package com.codewithmosh.store.mappers;

import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
  @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
  CartDto toDto(Cart cart);

  @Mapping(source="category.id", target="categoryId")
  ProductDto toDto(Product product);
}
