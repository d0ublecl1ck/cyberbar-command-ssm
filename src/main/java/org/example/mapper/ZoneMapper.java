package org.example.mapper;

import org.example.entity.Zone;
import java.util.List;

public interface ZoneMapper {
    List<Zone> selectAll();
    Zone selectById(Integer id);
    int insert(Zone zone);
    int update(Zone zone);
    int deleteById(Integer id);
} 