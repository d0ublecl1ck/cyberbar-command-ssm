package org.example.mapper;

import org.example.entity.Order;
import org.example.dto.OrderQueryDTO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {
    int insert(Order order);
    List<Order> selectByCondition(OrderQueryDTO queryDTO);
    Order selectById(Integer id);
    int updateStatus(@Param("id") Integer id, @Param("status") String status);
    int deleteById(Integer id);
} 