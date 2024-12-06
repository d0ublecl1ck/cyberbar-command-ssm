package org.example.service.impl;

import org.example.entity.Zone;
import org.example.mapper.ZoneMapper;
import org.example.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneServiceImpl implements ZoneService {
    @Autowired
    private ZoneMapper zoneMapper;
    
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
    public int deleteZone(Integer id) {
        return zoneMapper.deleteById(id);
    }
} 