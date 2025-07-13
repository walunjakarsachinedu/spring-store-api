package com.codewithmosh.store.orders.entities;

import com.codewithmosh.store.carts.entities.Cart;
import com.codewithmosh.store.users.entities.User;
import jakarta.persistence.*;
import lombok.*;

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

  @JoinColumn(name="customer_id")
  @ManyToOne
  private User customer;

  @Column(name="status")
  @Enumerated(EnumType.STRING)
  private OrderStatus status = OrderStatus.PENDING;

  @Column(name="created_at", insertable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name="total_price")
  private BigDecimal totalPrice = BigDecimal.ZERO;

  @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private Set<OrderItem> items = new LinkedHashSet<>();

  public boolean isOwnByUser(User user) {
    return getCustomer().getId().equals(user.getId());
  }

  static public Order from(Cart cart, User user) {
    var order = new Order();
    order.setStatus(OrderStatus.PENDING);
    order.setCustomer(user);
    order.setTotalPrice(cart.getTotalPrice());
    cart.getItems().forEach(item -> {
      order.items.add(new OrderItem(order, item.getProduct(), item.getQuantity() ));
    });
    return order;
  }
}
