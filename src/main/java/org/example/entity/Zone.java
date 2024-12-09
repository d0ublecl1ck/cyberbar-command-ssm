package org.example.entity;

/**
 * 区域实体类
 * 用于管理网吧不同区域的信息
 */
public class Zone {
    /** 区域ID */
    private Integer id;
    /** 区域名称 */
    private String name;
    
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
    
    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
} 