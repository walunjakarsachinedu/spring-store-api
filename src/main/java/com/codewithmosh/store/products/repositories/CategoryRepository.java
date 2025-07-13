package com.codewithmosh.store.products.repositories;

import com.codewithmosh.store.products.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}