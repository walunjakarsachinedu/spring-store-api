package com.codewithmosh.store.carts;

import com.codewithmosh.store.carts.dtos.AddItemToCartRequest;
import com.codewithmosh.store.carts.dtos.CartDto;
import com.codewithmosh.store.carts.dtos.UpdateCartRequest;
import com.codewithmosh.store.carts.exceptions.CartNotFoundException;
import com.codewithmosh.store.products.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@AllArgsConstructor
@RestController
@RequestMapping("/carts")
@Tag(name = "Carts")
public class CartController {
  CartMapper cartMapper;
  CartService cartService;

  @PostMapping(path = {"", "/"})
  public CartDto createCart() {
    var cart = cartService.createCart();
    return cartMapper.toDto(cart);
  }


  @GetMapping("/{cartId}/items")
  public CartDto getCart(
    @PathVariable("cartId") String cartId
  ) {
    var cart = cartService.getCartById(cartId);
    return cartMapper.toDto(cart);
  }

  @PostMapping("/{cartId}/items")
  @Operation(summary = "Add a product to the cart.")
  public CartDto addItem(
    @Parameter(description = "The ID of the cart.")
    @PathVariable("cartId") String cartId,
    @Valid @RequestBody AddItemToCartRequest request
  ) {
      var cart = cartService.addItem(cartId, request.getProductId());
      return cartMapper.toDto(cart);
  }

  @PutMapping("/{cartId}/items/{productId}")
  public CartDto updateItem(
    @PathVariable("cartId") String cartId,
    @PathVariable("productId") Long productId,
    @Valid @RequestBody UpdateCartRequest request
    ) {
    var cart = cartService.updateCartQuantity(
      cartId,
      productId,
      request.getQuantity()
    );

    return cartMapper.toDto(cart);
  }


  @DeleteMapping("/{cartId}/items/{productId}")
  public ResponseEntity<?> removeItem(
    @PathVariable("cartId") String cartId,
    @PathVariable("productId") Long productId
  ) {
    cartService.removeItem(cartId, productId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{cardId}/items")
  ResponseEntity<?> clearCart(
    @PathVariable("cardId") String cartId
  ) {
    cartService.clearCart(cartId);
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
