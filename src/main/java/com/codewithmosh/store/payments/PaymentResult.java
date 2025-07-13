package com.codewithmosh.store.payments;

import com.codewithmosh.store.orders.entities.OrderStatus;

public record PaymentResult(Long orderId, OrderStatus orderStatus) {
}
