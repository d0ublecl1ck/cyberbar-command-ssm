package org.example.service.impl;

import org.example.entity.Zone;
import org.example.mapper.ZoneMapper;
import org.example.service.ZoneService;
import org.example.service.MachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ZoneServiceImpl implements ZoneService {
    private static final Logger logger = LoggerFactory.getLogger(ZoneServiceImpl.class);
    
    @Autowired
    private ZoneMapper zoneMapper;
    
    @Autowired
    private MachineService machineService;
    
    @Override
    public List<Zone> getAllZones() {
        return zoneMapper.selectAll();
    }
    
    @Override
    public Zone getZoneById(Integer id) {
        return zoneMapper.selectById(id);
    }
    
    @Override
    public int createZone(Zone zone) {
        return zoneMapper.insert(zone);
    }
    
    @Override
    public int updateZone(Zone zone) {
        return zoneMapper.update(zone);
    }
    
    @Override
    @Transactional
    public int deleteZone(Integer id) {
        machineService.clearZoneId(id);
        return zoneMapper.deleteById(id);
    }
    
    @Override
    public Zone getZoneByName(String name) {
        logger.info("开始查询区域名称: [{}]", name);
        Zone zone = zoneMapper.selectByName(name);
        logger.info("查询结果: {}", zone != null ? "找到记录" : "未找到记录");
        return zone;
    }
} 