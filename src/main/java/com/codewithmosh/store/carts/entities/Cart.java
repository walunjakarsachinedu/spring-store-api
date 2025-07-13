package com.codewithmosh.store.carts.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Table(name = "carts")
public class Cart {
  @Id
  @GeneratedValue(strategy=GenerationType.UUID)
  @Column(name = "id")
  private UUID id;

  @Column(name = "created_at", insertable = false, updatable = false)
  private LocalDate dateCreated;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE, orphanRemoval = true)
  private Set<CartItem> items = new LinkedHashSet<>();

  public BigDecimal getTotalPrice() {
    BigDecimal totalPrice = BigDecimal.ZERO;
    return items.stream()
      .map(CartItem::getTotalPrice)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public CartItem getCartItem(Long productId) {
    return getItems()
      .stream()
      .filter(el -> el.getProduct().getId().equals(productId))
      .findFirst()
      .orElse(null);
  }

  public boolean isEmpty() {
    return getItems().isEmpty();
  }

  public void clearCart() {
    this.items.clear();
  }
}
