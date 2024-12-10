package org.example.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 商品查询数据传输对象
 */
@Data
public class CommodityQueryDTO {
    /** 商品名称（模糊查询） */
    private String name;
    /** 最低价格 */
    private BigDecimal minPrice;
    /** 最高价格 */
    private BigDecimal maxPrice;
    /** 当前页码 */
    private Integer pageNum = 1;
    /** 每页数量 */
    private Integer pageSize = 10;
} 