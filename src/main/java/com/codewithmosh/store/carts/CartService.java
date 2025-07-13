package com.codewithmosh.store.carts;

import com.codewithmosh.store.carts.entities.Cart;
import com.codewithmosh.store.carts.entities.CartItem;
import com.codewithmosh.store.carts.exceptions.CartNotFoundException;
import com.codewithmosh.store.products.entities.Product;
import com.codewithmosh.store.products.ProductNotFoundException;
import com.codewithmosh.store.products.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
    var cartItem = cart.getCartItem(productId);

    if(cartItem == null) {
      var product = getProductById(productId);
      cartItem = CartItem.builder()
          .product(product)
          .cart(cart)
          .quantity(0)
          .build();
      cart.getItems().add(
        cartItem
      );
    }

    cartItem.setQuantity(cartItem.getQuantity()+1);

    cartRepository.save(cart);

    return cart;
  }

  public Cart updateCartQuantity(String cartId, Long productId, Integer quantity) {
    var cart = getCartById(cartId);
    var item = cart.getCartItem(productId);

    if(item == null) throw new ProductNotFoundException();

    System.out.println("Log: setting product quantity");
    item.setQuantity(quantity);

    System.out.println("Log: saving cart");
    cartRepository.save(cart);

    return cart;
  }

  public void removeItem(String cartId, Long productId) {
    var cart = getCartById(cartId);
    var item = cart.getCartItem(productId);

    if(item == null) throw new ProductNotFoundException();

    cart.getItems().remove(item);
    item.setCart(null);

    cartRepository.save(cart);
  }

  public void clearCart(String cartId) {
    var cart = cartRepository.findById(UUID.fromString(cartId)).orElse(null);
    if(cart == null) throw new CartNotFoundException();

    cart.getItems().clear();
    cartRepository.save(cart);
  }
}
