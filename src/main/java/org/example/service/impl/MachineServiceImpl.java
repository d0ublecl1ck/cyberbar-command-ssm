package org.example.service.impl;

import org.example.context.AdminContext;
import org.example.entity.Machine;
import org.example.entity.ManagementLog;
import org.example.mapper.MachineMapper;
import org.example.service.MachineService;
import org.example.dto.MachineQueryDTO;
import org.example.service.ManagementLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.List;

@Service
public class MachineServiceImpl implements MachineService {
    private static final Logger logger = LoggerFactory.getLogger(MachineServiceImpl.class);
    
    @Autowired
    private MachineMapper machineMapper;
    
    @Autowired
    private ManagementLogService managementLogService;
    
    // 用于获取当前管理员ID的工具类或服务
    @Autowired
    private AdminContext adminContext;  // 这个需要您自己实现，用于获取当前登录的管理员ID
    
    @Override
    public List<Machine> getAllMachines() {
        logOperation("QUERY_MACHINE", "查询所有机器列表");
        return machineMapper.selectAll();
    }
    
    @Override
    public Machine getMachineById(Integer id) {
        logOperation("QUERY_MACHINE", String.format("查询机器详情，机器ID: %d", id));
        return machineMapper.selectById(id);
    }
    
    @Override
    public List<Machine> getMachinesByZoneId(Integer zoneId) {
        logOperation("QUERY_MACHINE", String.format("查询区域机器列表，区域ID: %d", zoneId));
        return machineMapper.selectByZoneId(zoneId);
    }
    
    @Override
    public int createMachine(Machine machine) {
        int result = machineMapper.insert(machine);
        if (result > 0) {
            logOperation("CREATE_MACHINE", String.format(
                "创建新机器，区域ID: %d, 价格: %.2f元/小时, 状态: %s",
                machine.getZoneId(),
                machine.getPrice(),
                machine.getStatus()
            ));
        }
        return result;
    }
    
    @Override
    public int updateMachine(Machine machine) {
        Machine currentMachine = machineMapper.selectById(machine.getId());
        if (currentMachine == null) {
            throw new RuntimeException("机器不存在");
        }
        
        // 如果机器被占用，且尝试修改价格，则抛出异常
        if ("Occupied".equals(currentMachine.getStatus()) 
            && machine.getPrice() != null 
            && !machine.getPrice().equals(currentMachine.getPrice())) {
            logOperation("UPDATE_MACHINE_ERROR", String.format(
                "尝试修改使用中机器价格失败，机器ID: %d, 当前价格: %.2f元/小时, 目标价格: %.2f元/小时",
                machine.getId(),
                currentMachine.getPrice(),
                machine.getPrice()
            ));
            throw new RuntimeException("机器使用中，不允许修改价格");
        }
        
        int result = machineMapper.update(machine);
        if (result > 0) {
            StringBuilder changes = new StringBuilder();
            if (machine.getZoneId() != null && !machine.getZoneId().equals(currentMachine.getZoneId())) {
                changes.append(String.format("区域ID: %d → %d; ", currentMachine.getZoneId(), machine.getZoneId()));
            }
            if (machine.getPrice() != null && !machine.getPrice().equals(currentMachine.getPrice())) {
                changes.append(String.format("价格: %.2f元/小时 → %.2f元/小时; ", currentMachine.getPrice(), machine.getPrice()));
            }
            if (machine.getStatus() != null && !machine.getStatus().equals(currentMachine.getStatus())) {
                changes.append(String.format("状态: %s → %s; ", currentMachine.getStatus(), machine.getStatus()));
            }
            
            logOperation("UPDATE_MACHINE", String.format(
                "更新机器信息，机器ID: %d, 更新内容: %s", 
                machine.getId(), 
                changes.toString()
            ));
        }
        return result;
    }
    
    @Override
    public int deleteMachine(Integer id) {
        Machine machine = machineMapper.selectById(id);
        if (machine != null) {
            int result = machineMapper.deleteById(id);
            if (result > 0) {
                logOperation("DELETE_MACHINE", String.format(
                    "删除机器，机器ID: %d, 区域ID: %d, 价格: %.2f元/小时, 状态: %s",
                    machine.getId(),
                    machine.getZoneId(),
                    machine.getPrice(),
                    machine.getStatus()
                ));
            }
            return result;
        }
        return 0;
    }
    
    @Override
    public int getTotalMachinesCount() {
        return machineMapper.countTotal();
    }
    
    @Override
    public int getCountByStatus(String status) {
        return machineMapper.countByStatus(status);
    }
    
    @Override
    public List<Machine> getMachinesByCondition(MachineQueryDTO queryDTO) {
        return machineMapper.selectByCondition(queryDTO);
    }

    @Override
    public int getTotalMachinesCountByZone(Integer zoneId) {
        return machineMapper.getTotalMachinesCountByZone(zoneId);
    }

    @Override
    public int getCountByStatusAndZone(String status, Integer zoneId) {
        return machineMapper.getCountByStatusAndZone(status, zoneId);
    }

    @Override
    public BigDecimal getAveragePrice() {
        return machineMapper.getAveragePrice();
    }

    @Override
    public BigDecimal getMaxPrice() {
        return machineMapper.getMaxPrice();
    }

    @Override
    public BigDecimal getMinPrice() {
        return machineMapper.getMinPrice();
    }

    @Override
    public int clearZoneId(Integer zoneId) {
        int result = machineMapper.updateZoneIdToNull(zoneId);
        if (result > 0) {
            logOperation("UPDATE_MACHINE_ZONE", String.format(
                "清除区域绑定，区域ID: %d, 影响机器数量: %d台",
                zoneId, 
                result
            ));
        }
        return result;
    }

    @Override
    public List<Machine> getMachinesWithoutZone(MachineQueryDTO queryDTO) {
        return machineMapper.selectByNullZoneId(queryDTO);
    }

    /**
     * 记录操作日志
     */
    private void logOperation(String operation, String detail) {
        try {
            ManagementLog log = new ManagementLog();
            log.setAdminId(adminContext.getCurrentAdminId());
            log.setOperation(operation);
            log.setDetail(detail);
            managementLogService.createManagementLog(log);
        } catch (Exception e) {
            // 记录日志失败不应影响主要业务
            logger.error("记录管理日志失败", e);
        }
    }
} 