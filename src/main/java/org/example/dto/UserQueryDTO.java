package org.example.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 用户查询数据传输对象
 * 用于封装用户查询的条件参数
 */
@Data
public class UserQueryDTO {
    /** 搜索关键字 */
    private String keyword;
    /** 用户名称 */
    private String name;
    /** 用户状态 */
    private String status;
    /** 最低余额 */
    private BigDecimal minBalance;
    /** 最高余额 */
    private BigDecimal maxBalance;
    /** 当前页码 */
    private Integer pageNum = 1;
    /** 每页数量 */
    private Integer pageSize = 10;
} 