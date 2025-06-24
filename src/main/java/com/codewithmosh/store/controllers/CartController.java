package com.codewithmosh.store.controllers;

import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.entities.dtos.AddItemToCartRequest;
import com.codewithmosh.store.entities.dtos.CartDto;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

/**
 1. createCart
 2. addToCart
 3. getCart
 4. updateItem
 5. removeItem
 6. clearCart
 */
@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
  CartRepository cartRepository;
  ProductRepository productRepository;
  CartMapper cartMapper;

  @PostMapping("/")
  public CartDto createCart() {
    var cart = new Cart();
    cartRepository.save(cart);
    System.out.println("created cart: " + cart);
    return cartMapper.toDto(cart);
  }

  @PostMapping("/{cart_id}/items")
  public ResponseEntity<?> addToCart(
    @PathVariable("cart_id") String cartId,
    @RequestBody AddItemToCartRequest request
  ) {
    var cart = cartRepository.findById(UUID.fromString(cartId)).orElse(null);
    System.out.println("Log: getting cart");
    if (cart == null) {
      return ResponseEntity.notFound().build();
    }

    System.out.println("Log: getting product");
    var product = productRepository.findById(request.getProductId()).orElse(null);
    if (product == null) {
      return ResponseEntity.badRequest().body("Product with given id not found.");
    }

    System.out.println("Log: count of items in cart: " + cart.getItems().size());
    var cartItem = cart.getItems().stream()
      .filter(item -> Objects.equals(item.getProduct().getId(), product.getId()))
      .findFirst()
      .orElse(null);

    if (cartItem == null) {
      System.out.println("Log: item not found, searching product");
      cartItem = CartItem.builder()
        .cart(cart)
        .quantity(0)
        .product(product)
        .build();
      System.out.println("Log: cartItem.getCart().getId(): " + cartItem.getCart().getId());
      cart.getItems().add(cartItem);
      System.out.println("Log: adding cart item");
    }

    System.out.println("Log: increasing quantity of cartItem");
    cartItem.setQuantity(cartItem.getQuantity() + 1);

    System.out.println("Log: saving cart");
    cartRepository.save(cart);

    return ResponseEntity.ok(cartMapper.toDto(cart));
  }

  @GetMapping("/{cart_id}/items")
  public ResponseEntity<?> getCart(
    @PathVariable("cart_id") String cartId
  ) {
    var cart = cartRepository.findById(UUID.fromString(cartId)).orElse(null);
    System.out.println("Log: getting cart");
    if (cart == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(cartMapper.toDto(cart));
  }

}
