package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.example.dto.OrderQueryDTO;
import org.example.entity.Order;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "订单管理接口")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("创建新订单")
    @PostMapping
    public ResponseEntity<Integer> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @ApiOperation("搜索订单")
    @GetMapping("/search")
    public ResponseEntity<PageInfo<Order>> searchOrders(
            @ApiParam("商品名称") @RequestParam(required = false) String commodityName,
            @ApiParam("开始日期") @RequestParam(required = false) LocalDateTime startDate,
            @ApiParam("结束日期") @RequestParam(required = false) LocalDateTime endDate,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {

        OrderQueryDTO queryDTO = new OrderQueryDTO();
        queryDTO.setCommodityName(commodityName);
        queryDTO.setStartDate(startDate);
        queryDTO.setEndDate(endDate);

        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders = orderService.getOrdersByCondition(queryDTO);

        return ResponseEntity.ok(new PageInfo<>(orders));
    }
} 