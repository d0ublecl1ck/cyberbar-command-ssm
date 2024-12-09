package org.example.service;

import org.example.entity.Machine;
import java.util.List;
import org.example.dto.MachineQueryDTO;

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
} 