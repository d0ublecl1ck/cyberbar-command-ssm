package org.example.service;

import org.example.entity.Machine;
import java.util.List;
import org.example.dto.MachineQueryDTO;
import java.math.BigDecimal;

public interface MachineService {
    List<Machine> getAllMachines();
    Machine getMachineById(Integer id);
    List<Machine> getMachinesByZoneId(Integer zoneId);
    int createMachine(Machine machine);
    int updateMachine(Machine machine);
    int deleteMachine(Integer id);
    int getTotalMachinesCount();
    int getCountByStatus(String status);
    List<Machine> getMachinesByCondition(MachineQueryDTO queryDTO);
    int getTotalMachinesCountByZone(Integer zoneId);
    int getCountByStatusAndZone(String status, Integer zoneId);
    BigDecimal getAveragePrice();
    BigDecimal getMaxPrice();
    BigDecimal getMinPrice();
    int clearZoneId(Integer zoneId);
    List<Machine> getMachinesWithoutZone(MachineQueryDTO queryDTO);
} 