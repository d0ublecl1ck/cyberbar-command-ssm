package org.example.service.impl;

import org.example.entity.Commodity;
import org.example.entity.Message;
import org.example.entity.Order;
import org.example.entity.OrderCommodity;
import org.example.mapper.OrderMapper;
import org.example.mapper.CommodityMapper;
import org.example.service.MessageService;
import org.example.service.OrderService;
import org.example.dto.OrderQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private CommodityMapper commodityMapper;
    
    @Autowired
    private MessageService messageService;

    @Override
    @Transactional
    public int createOrder(Order order) {
        // 检查并扣减库存
        for (OrderCommodity item : order.getCommodities()) {
            int result = commodityMapper.decreaseStock(item.getCommodityId(), item.getQuantity());
            if (result <= 0) {
                throw new RuntimeException("商品库存不足，商品ID: " + item.getCommodityId());
            }
        }
        
        // 创建订单
        return orderMapper.insert(order);
    }

    @Override
    public List<Order> getOrdersByCondition(OrderQueryDTO queryDTO) {
        return orderMapper.selectByCondition(queryDTO);
    }

    @Override
    public Order getOrderById(Integer id) {
        return orderMapper.selectById(id);
    }

    @Override
    @Transactional
    public boolean updateOrderStatus(Integer id, String status) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 如果订单被取消，恢复库存
        if ("Cancelled".equals(status)) {
            for (OrderCommodity item : order.getCommodities()) {
                commodityMapper.increaseStock(item.getCommodityId(), item.getQuantity());
            }
        }
        
        // 如果订单完成，检查库存
        if ("Completed".equals(status)) {
            StringBuilder lowStockMessage = new StringBuilder("以下商品库存不足5：\n");
            boolean hasLowStock = false;
            
            for (OrderCommodity item : order.getCommodities()) {
                Commodity commodity = commodityMapper.selectById(item.getCommodityId());
                if (commodity != null && commodity.getStock() < 5) {
                    hasLowStock = true;
                    lowStockMessage.append(String.format(
                        "- %s (ID: %d)：当前库存%d\n", 
                        commodity.getName(), 
                        commodity.getId(), 
                        commodity.getStock()
                    ));
                }
            }
            
            // 如果有库存不足的商品，创建提醒消息
            if (hasLowStock) {
                Message message = new Message();
                message.setContent(lowStockMessage.toString());
                message.setTime(LocalDateTime.now());
                message.setStatus("Pending");
                messageService.createMessage(message);
                logger.info("创建库存不足提醒：{}", lowStockMessage);
            }
        }
        
        return orderMapper.updateStatus(id, status) > 0;
    }

    @Override
    @Transactional
    public boolean deleteOrder(Integer id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return false;
        }
        
        // 如果订单还未完成就被删除，恢复库存
        if (!"Completed".equals(order.getStatus())) {
            for (OrderCommodity item : order.getCommodities()) {
                commodityMapper.increaseStock(item.getCommodityId(), item.getQuantity());
            }
        }
        
        return orderMapper.deleteById(id) > 0;
    }
} 