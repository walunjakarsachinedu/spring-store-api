package com.codewithmosh.store.products;


import com.codewithmosh.store.products.dtos.CreateProductRequest;
import com.codewithmosh.store.products.dtos.ProductDto;
import com.codewithmosh.store.products.dtos.UpdateProductRequest;
import com.codewithmosh.store.products.entities.Product;
import com.codewithmosh.store.products.repositories.CategoryRepository;
import com.codewithmosh.store.products.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
  private ProductMapper productMapper;
  private ProductRepository productRepository;
  private CategoryRepository categoryRepository;

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

  @PostMapping
  public ResponseEntity<?> createProduct(
    @RequestBody CreateProductRequest request,
    UriComponentsBuilder uriBuilder
  ) {
    var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
    if(category == null) return ResponseEntity.badRequest().body("Provided category not exists, please provide existing category id");

    var product = productMapper.toEntity(request);
    product.setCategory(category);
    productRepository.save(product);

    var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
    return ResponseEntity.created(uri).body(productMapper.toDto(product));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateProduct(
    @PathVariable("id") Long id,
    @RequestBody UpdateProductRequest request
  ) {
    var product = productRepository.findById(id).orElse(null);
    if(product == null) return ResponseEntity.notFound().build();

    var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
    if(category == null) return ResponseEntity.badRequest().body("Provided category not exists, please provide existing category id");

    productMapper.update(request, product);
    product.setCategory(category);
    productRepository.save(product);

    return ResponseEntity.ok(productMapper.toDto(product));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
    var product = productRepository.findById(id).orElse(null);
    if(product == null) return ResponseEntity.notFound().build();

    productRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
