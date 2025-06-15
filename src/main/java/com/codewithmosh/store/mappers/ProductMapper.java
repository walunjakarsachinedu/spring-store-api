package com.codewithmosh.store.mappers;

import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.entities.dtos.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ProductMapper {
  @Mapping(source="category.id", target="categoryId")
  ProductDto toDto(Product product);
}
