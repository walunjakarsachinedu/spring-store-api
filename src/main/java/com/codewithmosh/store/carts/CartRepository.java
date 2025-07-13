package com.codewithmosh.store.carts;

import com.codewithmosh.store.carts.entities.Cart;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CartRepository extends CrudRepository<Cart, UUID> {
}
