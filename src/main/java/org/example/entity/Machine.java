package org.example.entity;

/**
 * 机器实体类
 * 用于存储网吧电脑设备的基本信息
 */
public class Machine {
    /** 机器ID */
    private Integer id;
    /** 所属区域ID */
    private Integer zoneId;
    /** 每小时价格 */
    private Double price;
    /** 机器状态（Abnormal-异常/Idle-空闲/Occupied-使用中） */
    private String status;
    /** 当前使用该机器的用户ID */
    private Integer onlineUserId;
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getZoneId() {
        return zoneId;
    }
    
    public void setZoneId(Integer zoneId) {
        this.zoneId = zoneId;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getOnlineUserId() {
        return onlineUserId;
    }
    
    public void setOnlineUserId(Integer onlineUserId) {
        this.onlineUserId = onlineUserId;
    }
} 