package com.codewithmosh.store.controllers;


import com.codewithmosh.store.dtos.ErrorDto;
import com.codewithmosh.store.dtos.OrderDto;
import com.codewithmosh.store.exceptions.OrderAccessDeniedException;
import com.codewithmosh.store.exceptions.OrderNotFoundException;
import com.codewithmosh.store.mappers.OrderMapper;
import com.codewithmosh.store.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping("/{order_id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("order_id") Long orderId) {
        var order = orderService.getOrder(orderId);
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    @GetMapping
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders().stream().map(orderMapper::toDto).toList();
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleOrderNotFoundException(Exception exp) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(OrderAccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleOrderAccessDeniedException(Exception exp) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(exp.getMessage()));
    }
}
