package org.example.service;

import org.example.dto.ManagementLogQueryDTO;
import org.example.entity.ManagementLog;
import com.github.pagehelper.PageInfo;

public interface ManagementLogService {
    int createManagementLog(ManagementLog managementLog);
    int deleteManagementLog(Integer id);
    int updateManagementLog(ManagementLog managementLog);
    PageInfo<ManagementLog> getManagementLogs(ManagementLogQueryDTO queryDTO);
} 