package org.example.service.impl;

import org.example.entity.Machine;
import org.example.mapper.MachineMapper;
import org.example.service.MachineService;
import org.example.dto.MachineQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineServiceImpl implements MachineService {
    @Autowired
    private MachineMapper machineMapper;
    
    @Override
    public List<Machine> getAllMachines() {
        return machineMapper.selectAll();
    }
    
    @Override
    public Machine getMachineById(Integer id) {
        return machineMapper.selectById(id);
    }
    
    @Override
    public List<Machine> getMachinesByZoneId(Integer zoneId) {
        return machineMapper.selectByZoneId(zoneId);
    }
    
    @Override
    public int createMachine(Machine machine) {
        return machineMapper.insert(machine);
    }
    
    @Override
    public int updateMachine(Machine machine) {
        return machineMapper.update(machine);
    }
    
    @Override
    public int deleteMachine(Integer id) {
        return machineMapper.deleteById(id);
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
} 