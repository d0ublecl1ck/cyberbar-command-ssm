package org.example.service.impl;

import org.example.entity.Order;
import org.example.mapper.OrderMapper;
import org.example.service.OrderService;
import org.example.dto.OrderQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public int createOrder(Order order) {
        return orderMapper.insert(order);
    }

    @Override
    public List<Order> getOrdersByCondition(OrderQueryDTO queryDTO) {
        return orderMapper.selectByCondition(queryDTO);
    }
} 