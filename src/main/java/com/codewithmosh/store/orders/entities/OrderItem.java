package com.codewithmosh.store.orders.entities;

import com.codewithmosh.store.products.entities.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
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

  public OrderItem(Order order, Product product, Integer quantity) {
    this.order = order;
    this.product = product;
    this.quantity = quantity;
    this.unitPrice = product.getPrice();
    this.totalPrice = this.unitPrice.multiply(BigDecimal.valueOf(quantity));
  }
}
