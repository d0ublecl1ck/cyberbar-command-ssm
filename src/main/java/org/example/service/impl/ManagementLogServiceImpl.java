package org.example.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.dto.ManagementLogQueryDTO;
import org.example.entity.ManagementLog;
import org.example.mapper.ManagementLogMapper;
import org.example.service.ManagementLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagementLogServiceImpl implements ManagementLogService {
    
    @Autowired
    private ManagementLogMapper managementLogMapper;
    
    @Override
    public int createManagementLog(ManagementLog managementLog) {
        return managementLogMapper.insert(managementLog);
    }
    
    @Override
    public int deleteManagementLog(Integer id) {
        return managementLogMapper.deleteById(id);
    }
    
    @Override
    public int updateManagementLog(ManagementLog managementLog) {
        return managementLogMapper.update(managementLog);
    }
    
    @Override
    public PageInfo<ManagementLog> getManagementLogs(ManagementLogQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        return new PageInfo<>(managementLogMapper.selectByCondition(queryDTO));
    }
} 