package com.codewithmosh.store.entities;

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

  @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE)
  private Set<CartItem> items = new LinkedHashSet<>();

  public BigDecimal getTotalPrice() {
    BigDecimal totalPrice = BigDecimal.ZERO;
    return items.stream()
      .map(CartItem::getTotalPrice)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
