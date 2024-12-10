package org.example.entity;

import java.math.BigDecimal;

/**
 * 商品实体类
 */
public class Commodity {
    /** 商品ID */
    private Integer id;
    /** 商品名称 */
    private String name;
    /** 商品价格 */
    private BigDecimal price;
    /** 计量单位 */
    private String unit;
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
} 