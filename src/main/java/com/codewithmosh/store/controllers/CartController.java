package com.codewithmosh.store.controllers;

import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.UpdateCartRequest;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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

  @PutMapping("/{cartId}/items/{productId}")
  public CartDto updateItem(
    @PathVariable("cartId") String cartId,
    @PathVariable("productId") Long productId,
    @Valid @RequestBody UpdateCartRequest request
    ) {
    var cart = cartRepository.findById(UUID.fromString(cartId)).orElse(null);
    System.out.println("Log: getting cart");
    if (cart == null) {
      throw new CartNotFoundException();
    }

    var item = cart
      .getItems()
      .stream()
      .filter(el -> Objects.equals(el.getProduct().getId(), productId))
      .findFirst().orElse(null);

    if(item == null) {
      throw new ProductNotFoundException();
    }

    System.out.println("Log: setting product quantity");
    item.setQuantity(request.getQuantity());

    System.out.println("Log: saving cart");
    cartRepository.save(cart);

    return cartMapper.toDto(cart);
  }


  @DeleteMapping("/{cartId}/items/{productId}")
  public ResponseEntity<?> removeItem(
    @PathVariable("cartId") String cartId,
    @PathVariable("productId") Long productId
  ) {
    var cart = cartRepository.findById(UUID.fromString(cartId)).orElse(null);
    System.out.println("Log: getting cart");
    if (cart == null) {
      throw new CartNotFoundException();
    }

    var item = cart
      .getItems()
      .stream()
      .filter(el -> Objects.equals(el.getProduct().getId(), productId))
      .findFirst().orElse(null);

    if(item == null) {
      System.out.println("Log: throwing product not found exception");
      throw new ProductNotFoundException();
    }
    System.out.println("Log: product found");

    System.out.println("Log: removing item");
    System.out.println("Log: total item [before removal]: " + cart.getItems().size());
    cart.getItems().remove(item);
    item.setCart(null);
    System.out.println("Log: total item [after removal]: " + cart.getItems().size());

    cartRepository.save(cart);

    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{cardId}/items")
  ResponseEntity<?> clearCart(
    @PathVariable("cardId") String cartId
  ) {
    var cart = cartRepository.findById(UUID.fromString(cartId)).orElse(null);
    if(cart == null) throw new CartNotFoundException();

    cart.getItems().clear();
    cartRepository.save(cart);

    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(CartNotFoundException.class)
  public ResponseEntity<?> cartNotFoundHandler() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found."));
  }

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<?> productNotFoundHandler() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product not found."));
  }
}
