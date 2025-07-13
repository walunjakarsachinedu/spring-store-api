package com.codewithmosh.store.products.repositories;

import com.codewithmosh.store.products.entities.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
  @EntityGraph(attributePaths = "category")
  List<Product> findByCategoryId(Byte categoryId);

  @EntityGraph(attributePaths = "category")
  @Query("select p from Product p")
  List<Product> findAllWithCategory();
}