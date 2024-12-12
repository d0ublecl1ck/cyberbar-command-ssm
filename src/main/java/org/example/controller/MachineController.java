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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

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
    
    @ApiOperation("搜索机器")
    @GetMapping("/search")
    public ResponseEntity<PageInfo<Machine>> searchMachines(
            @ApiParam("区域ID") @RequestParam(required = false) Integer zoneId,
            @ApiParam("最低价格") @RequestParam(required = false) BigDecimal minPrice,
            @ApiParam("最高价格") @RequestParam(required = false) BigDecimal maxPrice,
            @ApiParam("状态(Abnormal/Idle/Occupied)") @RequestParam(required = false) String status,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        
        logger.info("搜索机器，筛选条件：zoneId={}, price={}-{}, status={}, pageNum={}, pageSize={}", 
                zoneId, minPrice, maxPrice, status, pageNum, pageSize);
        
        MachineQueryDTO queryDTO = new MachineQueryDTO();
        queryDTO.setZoneId(zoneId);
        queryDTO.setMinPrice(minPrice);
        queryDTO.setMaxPrice(maxPrice);
        queryDTO.setStatus(status);
        
        PageHelper.startPage(pageNum, pageSize);
        List<Machine> machines = machineService.getMachinesByCondition(queryDTO);
        
        return ResponseEntity.ok(new PageInfo<>(machines));
    }
    
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
    public ResponseEntity<Map<String, Object>> updateMachine(
            @ApiParam(value = "机器ID", required = true) @PathVariable Integer id,
            @RequestBody Machine machine) {
        Map<String, Object> response = new HashMap<>();
        try {
            machine.setId(id);
            int result = machineService.updateMachine(machine);
            if (result > 0) {
                response.put("message", "更新成功");
                return ResponseEntity.ok(response);
            }
            response.put("message", "机器不存在");
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @ApiOperation("删除机器")
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteMachine(
            @ApiParam(value = "机器ID", required = true) @PathVariable Integer id) {
        return ResponseEntity.ok(machineService.deleteMachine(id));
    }
    
    @ApiOperation("获取机器统计信息")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getMachineStats(
            @ApiParam("区域ID") @RequestParam(required = false) Integer zoneId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 总体统计
        stats.put("totalMachines", machineService.getTotalMachinesCount());
        stats.put("abnormalMachines", machineService.getCountByStatus("Abnormal"));
        stats.put("idleMachines", machineService.getCountByStatus("Idle"));
        stats.put("occupiedMachines", machineService.getCountByStatus("Occupied"));
        
        // 如果指定了区域，添加该区域的统计信息
        if (zoneId != null) {
            stats.put("zoneTotalMachines", machineService.getTotalMachinesCountByZone(zoneId));
            stats.put("zoneAbnormalMachines", machineService.getCountByStatusAndZone("Abnormal", zoneId));
            stats.put("zoneIdleMachines", machineService.getCountByStatusAndZone("Idle", zoneId));
            stats.put("zoneOccupiedMachines", machineService.getCountByStatusAndZone("Occupied", zoneId));
        }
        
        // 添加价格统计信息
        stats.put("averagePrice", machineService.getAveragePrice());
        stats.put("maxPrice", machineService.getMaxPrice());
        stats.put("minPrice", machineService.getMinPrice());
        
        return ResponseEntity.ok(stats);
    }
    
    @ApiOperation("获取所有未绑定区域的机器")
    @GetMapping("/unbound")
    public ResponseEntity<PageInfo<Machine>> getUnboundMachines(
            @ApiParam("最低价格") @RequestParam(required = false) BigDecimal minPrice,
            @ApiParam("最高价格") @RequestParam(required = false) BigDecimal maxPrice,
            @ApiParam("状态(Abnormal/Idle/Occupied)") @RequestParam(required = false) String status,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        
        logger.info("获取未绑定机器，筛选条件：price={}-{}, status={}, pageNum={}, pageSize={}", 
                minPrice, maxPrice, status, pageNum, pageSize);
        
        MachineQueryDTO queryDTO = new MachineQueryDTO();
        queryDTO.setMinPrice(minPrice);
        queryDTO.setMaxPrice(maxPrice);
        queryDTO.setStatus(status);
        
        PageHelper.startPage(pageNum, pageSize);
        List<Machine> machines = machineService.getMachinesWithoutZone(queryDTO);
        
        return ResponseEntity.ok(new PageInfo<>(machines));
    }
} 