package org.example.mapper;

import org.example.entity.ManagementLog;
import org.example.dto.ManagementLogQueryDTO;
import java.util.List;

public interface ManagementLogMapper {
    int insert(ManagementLog managementLog);
    int deleteById(Integer id);
    int update(ManagementLog managementLog);
    List<ManagementLog> selectByCondition(ManagementLogQueryDTO queryDTO);
} 