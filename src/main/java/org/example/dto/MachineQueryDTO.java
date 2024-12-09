package org.example.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 机器查询数据传输对象
 * 用于封装机器查询的条件参数
 */
@Data
public class MachineQueryDTO {
    /** 区域ID */
    private Integer zoneId;
    /** 机器状态 */
    private String status;
    /** 最低价格 */
    private BigDecimal minPrice;
    /** 最高价格 */
    private BigDecimal maxPrice;
    /** 当前页码 */
    private Integer pageNum = 1;
    /** 每页数量 */
    private Integer pageSize = 10;
} 