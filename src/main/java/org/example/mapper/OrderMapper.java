package org.example.mapper;

import org.example.entity.Order;
import org.example.dto.OrderQueryDTO;
import java.util.List;

public interface OrderMapper {
    int insert(Order order);
    List<Order> selectByCondition(OrderQueryDTO queryDTO);
} 