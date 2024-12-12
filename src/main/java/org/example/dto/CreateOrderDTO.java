package org.example.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.DecimalMin;

@Data
public class CreateOrderDTO {
    @NotNull(message = "机器ID不能为空")
    private Integer machineId;
    
    @NotNull(message = "用户ID不能为空")
    private Integer userId;
    
    @NotEmpty(message = "商品列表不能为空")
    private List<OrderCommodityDTO> commodities;
    
    @NotNull(message = "总价不能为空")
    @DecimalMin(value = "0.01", message = "总价必须大于0")
    private BigDecimal totalPrice;
} 