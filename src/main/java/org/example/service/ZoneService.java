package org.example.service;

import org.example.entity.Zone;
import java.util.List;

public interface ZoneService {
    List<Zone> getAllZones();
    Zone getZoneById(Integer id);
    int createZone(Zone zone);
    int updateZone(Zone zone);
    int deleteZone(Integer id);
} 