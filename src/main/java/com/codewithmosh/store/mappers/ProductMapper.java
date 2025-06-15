package com.codewithmosh.store.mappers;

import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.entities.dtos.CreateProductRequest;
import com.codewithmosh.store.entities.dtos.ProductDto;
import com.codewithmosh.store.entities.dtos.UpdateProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface ProductMapper {
  @Mapping(source="category.id", target="categoryId")
  ProductDto toDto(Product product);
  Product toEntity(CreateProductRequest request);
  void update(UpdateProductRequest productDto, @MappingTarget Product product);
}
