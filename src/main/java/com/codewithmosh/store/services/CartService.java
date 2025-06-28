package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {
  CartRepository cartRepository;
  ProductRepository productRepository;
  CartMapper cartMapper;

  public Cart createCart() {
    var cart = new Cart();
    cartRepository.save(cart);
    System.out.println("created cart: " + cart);
    return cart;
  }

  public Cart getCartById(String cartId) {
    var cart = cartRepository.findById(UUID.fromString(cartId)).orElse(null);
    if (cart == null) throw new CartNotFoundException();
    return cart;
  }

  public Product getProductById(Long productId) {
    var product = productRepository.findById(productId).orElse(null);
    if (product == null) throw new ProductNotFoundException();
    return product;
  }

  public Cart addItem(String cartId, Long productId) {
    var cart = getCartById(cartId);

    var cartItem = cart.getItems()
      .stream()
      .filter(el -> el.getProduct().getId().equals(productId))
      .findFirst()
      .orElse(null);

    if(cartItem != null) {
      cartItem.setQuantity(cartItem.getQuantity()+1);
    }
    else {
      var product = getProductById(productId);
      cart.getItems().add(
        CartItem.builder()
          .product(product)
          .cart(cart)
          .quantity(0)
          .build()
      );
    }

    cartRepository.save(cart);

    return cart;
  }

  public Cart updateCartQuantity(String cartId, Long productId, Integer quantity) {
    var cart = getCartById(cartId);

    var item = cart
      .getItems()
      .stream()
      .filter(el -> Objects.equals(el.getProduct().getId(), productId))
      .findFirst().orElse(null);

    if(item == null) throw new ProductNotFoundException();

    System.out.println("Log: setting product quantity");
    item.setQuantity(quantity);

    System.out.println("Log: saving cart");
    cartRepository.save(cart);

    return cart;
  }

  public void removeItem(String cartId, Long productId) {
    var cart = getCartById(cartId);

    var item = cart
      .getItems()
      .stream()
      .filter(el -> Objects.equals(el.getProduct().getId(), productId))
      .findFirst().orElse(null);

    if(item == null) throw new ProductNotFoundException();

    System.out.println("Log: removing item");
    System.out.println("Log: total item [before removal]: " + cart.getItems().size());
    cart.getItems().remove(item);
    item.setCart(null);
    System.out.println("Log: total item [after removal]: " + cart.getItems().size());

    cartRepository.save(cart);
  }

  public void clearCart(String cartId) {
    var cart = cartRepository.findById(UUID.fromString(cartId)).orElse(null);
    if(cart == null) throw new CartNotFoundException();

    cart.getItems().clear();
    cartRepository.save(cart);
  }
}
