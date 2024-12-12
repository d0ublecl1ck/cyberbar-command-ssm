package org.example.service;

import org.example.entity.Order;
import org.example.dto.OrderQueryDTO;
import java.util.List;

public interface OrderService {
    int createOrder(Order order);
    List<Order> getOrdersByCondition(OrderQueryDTO queryDTO);
    Order getOrderById(Integer id);
    boolean updateOrderStatus(Integer id, String status);
    boolean deleteOrder(Integer id);
} 