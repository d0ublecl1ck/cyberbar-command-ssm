package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.example.dto.ManagementLogQueryDTO;
import org.example.entity.ManagementLog;
import org.example.service.ManagementLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;

@Api(tags = "管理日志接口")
@RestController
@RequestMapping("/api/managementlogs")
public class ManagementLogController {
    
    @Autowired
    private ManagementLogService managementLogService;
    
    @ApiOperation("创建管理日志")
    @PostMapping
    public ResponseEntity<Integer> createManagementLog(@RequestBody ManagementLog managementLog) {
        return ResponseEntity.ok(managementLogService.createManagementLog(managementLog));
    }
    
    @ApiOperation("删除管理日志")
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteManagementLog(
            @ApiParam(value = "日志ID", required = true) @PathVariable Integer id) {
        return ResponseEntity.ok(managementLogService.deleteManagementLog(id));
    }
    
    @ApiOperation("更新管理日志")
    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateManagementLog(
            @ApiParam(value = "日志ID", required = true) @PathVariable Integer id,
            @RequestBody ManagementLog managementLog) {
        managementLog.setId(id);
        return ResponseEntity.ok(managementLogService.updateManagementLog(managementLog));
    }
    
    @ApiOperation("条件查询管理日志")
    @GetMapping
    public ResponseEntity<PageInfo<ManagementLog>> getManagementLogs(
            @ApiParam("管理员ID") @RequestParam(required = false) Integer adminId,
            @ApiParam("开始时间") @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        
        ManagementLogQueryDTO queryDTO = new ManagementLogQueryDTO();
        queryDTO.setAdminId(adminId);
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);
        
        return ResponseEntity.ok(managementLogService.getManagementLogs(queryDTO));
    }
} 