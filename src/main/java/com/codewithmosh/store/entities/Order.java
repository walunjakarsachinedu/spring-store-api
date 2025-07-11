package com.codewithmosh.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id")
  private Long id;

  @Column(name="customer_id")
  @ManyToOne
  private User customer;

  @Column(name="status")
  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @Column(name="created_at", insertable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name="total_price")
  private BigDecimal totalPrice;

  @OneToMany(mappedBy = "order")
  private Set<OrderItem> items = new LinkedHashSet<>();
}
