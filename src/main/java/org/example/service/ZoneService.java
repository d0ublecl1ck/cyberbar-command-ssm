package org.example.service;

import org.example.entity.Zone;
import java.util.List;

/**
 * 区域服务接口
 */
public interface ZoneService {
    /**
     * 获取所有区域
     * @return 区域列表
     */
    List<Zone> getAllZones();
    
    /**
     * 根据ID获取区域
     * @param id 区域ID
     * @return 区域信息
     */
    Zone getZoneById(Integer id);
    
    int createZone(Zone zone);
    int updateZone(Zone zone);
    int deleteZone(Integer id);
    Zone getZoneByName(String name);
} 