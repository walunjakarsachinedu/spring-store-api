package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.OrderStatus;

public record PaymentResult(Long orderId, OrderStatus orderStatus) {
}
