package com.codewithmosh.store.orders;

import com.codewithmosh.store.orders.dtos.OrderDto;
import com.codewithmosh.store.orders.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
  OrderDto toDto(Order order);
}
