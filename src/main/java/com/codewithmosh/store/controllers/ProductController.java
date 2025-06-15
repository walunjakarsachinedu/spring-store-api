package com.codewithmosh.store.controllers;


import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.entities.dtos.ProductDto;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
  private ProductMapper productMapper;
  private ProductRepository productRepository;

  @GetMapping
  public List<ProductDto> getProductByCategory(@RequestParam(value = "categoryId", required = false) Byte categoryId) {
    System.out.println("getProductByCategory(category) : " + categoryId);
    List<Product> products;
    if(categoryId == null) products = productRepository.findAllWithCategory();
    else products = productRepository.findByCategoryId(categoryId);
    return products
      .stream()
      .map(productMapper::toDto)
      .toList();
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {
    var product = productRepository.findById(productId).orElse(null);
    if(product == null) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(productMapper.toDto(product));
  }
}
