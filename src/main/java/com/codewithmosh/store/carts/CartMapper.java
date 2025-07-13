package com.codewithmosh.store.carts;

import com.codewithmosh.store.carts.dtos.CartDto;
import com.codewithmosh.store.carts.entities.Cart;
import com.codewithmosh.store.products.entities.Product;
import com.codewithmosh.store.products.dtos.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
  @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
  CartDto toDto(Cart cart);

  @Mapping(source="category.id", target="categoryId")
  ProductDto toDto(Product product);
}
