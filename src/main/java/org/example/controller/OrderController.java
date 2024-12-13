package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.example.dto.CreateOrderDTO;
import org.example.dto.OrderQueryDTO;
import org.example.dto.OrderCommodityDTO;
import org.example.entity.Order;
import org.example.entity.OrderCommodity;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Arrays;

@Api(tags = "订单管理接口")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @ApiOperation("创建新订单")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@Validated @RequestBody CreateOrderDTO createOrderDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            Order order = new Order();
            order.setMachineId(createOrderDTO.getMachineId());
            order.setUserId(createOrderDTO.getUserId());
            order.setTotalPrice(createOrderDTO.getTotalPrice());
            order.setOrderDate(LocalDateTime.now());
            order.setStatus("Pending");
            
            // 转换商品列表
            List<OrderCommodity> commodities = createOrderDTO.getCommodities().stream()
                .map(this::convertToOrderCommodity)
                .collect(Collectors.toList());
            order.setCommodities(commodities);
            
            int orderId = orderService.createOrder(order);
            
            response.put("message", "订单创建成功");
            response.put("orderId", orderId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("创建订单失败", e);
            response.put("message", "创建订单失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // DTO 转换为实体的辅助方法
    private OrderCommodity convertToOrderCommodity(OrderCommodityDTO dto) {
        OrderCommodity commodity = new OrderCommodity();
        commodity.setCommodityId(dto.getCommodityId());
        commodity.setName(dto.getName());
        commodity.setQuantity(dto.getQuantity());
        commodity.setPrice(dto.getPrice());
        return commodity;
    }

    @ApiOperation("搜索订单")
    @GetMapping("/search")
    public ResponseEntity<PageInfo<Order>> searchOrders(
            @ApiParam("商品名称") @RequestParam(required = false) String commodityName,
            @ApiParam("开始日期") @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @ApiParam("结束日期") @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @ApiParam("订单状态") @RequestParam(required = false) String status,
            @ApiParam("用户ID") @RequestParam(required = false) Integer userId,
            @ApiParam("机器ID") @RequestParam(required = false) Integer machineId,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        
        OrderQueryDTO queryDTO = new OrderQueryDTO();
        queryDTO.setCommodityName(commodityName);
        queryDTO.setStartDate(startDate);
        queryDTO.setEndDate(endDate);
        queryDTO.setStatus(status);
        queryDTO.setUserId(userId);
        queryDTO.setMachineId(machineId);
        
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders = orderService.getOrdersByCondition(queryDTO);
        
        return ResponseEntity.ok(new PageInfo<>(orders));
    }

    @ApiOperation("获取订单详情")
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer id) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation("更新订单状态")
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        Map<String, Object> response = new HashMap<>();
        
        // 验证状态值是否有效
        if (!Arrays.asList("Pending", "Completed", "Cancelled").contains(status)) {
            response.put("message", "无效的订单状态");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            if (orderService.updateOrderStatus(id, status)) {
                response.put("message", "订单状态更新成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "订单不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("更新订单状态失败", e);
            response.put("message", "更新订单状态失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @ApiOperation("删除订单")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteOrder(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (orderService.deleteOrder(id)) {
                response.put("message", "订单删除成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "订单不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("删除订单失败", e);
            response.put("message", "删除订单失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 