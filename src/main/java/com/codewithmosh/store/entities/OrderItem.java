package com.codewithmosh.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name="order_items")
public class OrderItem {
  @Id
  @Column(name="id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name="order_id")
  @ManyToOne
  private Order order;

  @JoinColumn(name="product_id")
  @ManyToOne
  private Product product;

  @Column(name="unit_price")
  private BigDecimal unitPrice;

  @Column(name="quantity")
  private Integer quantity;

  @Column(name="total_price")
  private BigDecimal totalPrice;


  static public OrderItem from(CartItem cartItem, Order order) {
    return OrderItem.builder()
      .order(order)
      .product(cartItem.getProduct())
      .unitPrice(cartItem.getProduct().getPrice())
      .quantity(cartItem.getQuantity())
      .totalPrice(cartItem.getTotalPrice())
      .build();
  }
}
