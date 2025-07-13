package com.codewithmosh.store.products;

import com.codewithmosh.store.products.dtos.CreateProductRequest;
import com.codewithmosh.store.products.dtos.ProductDto;
import com.codewithmosh.store.products.dtos.UpdateProductRequest;
import com.codewithmosh.store.products.entities.Product;
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
