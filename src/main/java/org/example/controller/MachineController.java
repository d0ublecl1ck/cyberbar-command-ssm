package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.example.dto.MachineQueryDTO;
import org.example.entity.Machine;
import org.example.service.MachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;

@Api(tags = "机器管理接口")
@RestController
@RequestMapping("/api/machines")
public class MachineController {
    private static final Logger logger = LoggerFactory.getLogger(MachineController.class);
    
    @Autowired
    private MachineService machineService;
    
    @ApiOperation("获取所有机器")
    @GetMapping
    public ResponseEntity<List<Machine>> getAllMachines(
            @ApiParam("区域ID") @RequestParam(required = false) Integer zoneId,
            @ApiParam("最低价格") @RequestParam(required = false) BigDecimal minPrice,
            @ApiParam("最高价格") @RequestParam(required = false) BigDecimal maxPrice,
            @ApiParam("状态(Abnormal/Idle/Occupied)") @RequestParam(required = false) String status) {
        logger.info("获取机器列表，筛选条件：zoneId={}, price={}-{}, status={}", 
                zoneId, minPrice, maxPrice, status);
        
        MachineQueryDTO queryDTO = new MachineQueryDTO();
        queryDTO.setZoneId(zoneId);
        queryDTO.setMinPrice(minPrice);
        queryDTO.setMaxPrice(maxPrice);
        queryDTO.setStatus(status);
        
        return ResponseEntity.ok(machineService.getMachinesByCondition(queryDTO));
    }
    
    @ApiOperation("根据ID获取机器")
    @GetMapping("/{id}")
    public ResponseEntity<Machine> getMachineById(
            @ApiParam(value = "机器ID", required = true) @PathVariable Integer id) {
        Machine machine = machineService.getMachineById(id);
        if (machine != null) {
            return ResponseEntity.ok(machine);
        }
        return ResponseEntity.notFound().build();
    }
    
    @ApiOperation("根据区域ID获取机器列表")
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<Machine>> getMachinesByZoneId(
            @ApiParam(value = "区域ID", required = true) @PathVariable Integer zoneId) {
        return ResponseEntity.ok(machineService.getMachinesByZoneId(zoneId));
    }
    
    @ApiOperation("创建新机器")
    @PostMapping
    public ResponseEntity<Integer> createMachine(@RequestBody Machine machine) {
        return ResponseEntity.ok(machineService.createMachine(machine));
    }
    
    @ApiOperation("更新机器信息")
    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateMachine(
            @ApiParam(value = "机器ID", required = true) @PathVariable Integer id,
            @RequestBody Machine machine) {
        machine.setId(id);
        return ResponseEntity.ok(machineService.updateMachine(machine));
    }
    
    @ApiOperation("删除机器")
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteMachine(
            @ApiParam(value = "机器ID", required = true) @PathVariable Integer id) {
        return ResponseEntity.ok(machineService.deleteMachine(id));
    }
    
    @ApiOperation("获取机器统计信息")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getMachineStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMachines", machineService.getTotalMachinesCount());
        stats.put("abnormalMachines", machineService.getCountByStatus("Abnormal"));
        stats.put("idleMachines", machineService.getCountByStatus("Idle"));
        stats.put("occupiedMachines", machineService.getCountByStatus("Occupied"));
        return ResponseEntity.ok(stats);
    }
} 