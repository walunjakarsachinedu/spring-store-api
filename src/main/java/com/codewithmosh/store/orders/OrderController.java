package com.codewithmosh.store.orders;


import com.codewithmosh.store.common.ErrorDto;
import com.codewithmosh.store.orders.dtos.OrderDto;
import com.codewithmosh.store.orders.dtos.OrderNotFoundException;
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
