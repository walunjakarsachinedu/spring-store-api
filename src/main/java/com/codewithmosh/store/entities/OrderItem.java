package com.codewithmosh.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name="order_items")
public class OrderItem {
  @Id
  @Column(name="id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="order_id")
  @ManyToOne
  private Order order;

  @Column(name="product_id")
  @ManyToOne
  private Product product;

  @Column(name="unit_price")
  private BigDecimal unitPrice;

  @Column(name="quantity")
  private Integer quantity;

  @Column(name="total_price")
  private BigDecimal totalPrice;
}
