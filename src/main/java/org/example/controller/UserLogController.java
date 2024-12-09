package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.example.dto.UserLogQueryDTO;
import org.example.entity.UserLog;
import org.example.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;

@Api(tags = "用户日志接口")
@RestController
@RequestMapping("/api/userlogs")
public class UserLogController {
    
    @Autowired
    private UserLogService userLogService;
    
    @ApiOperation("创建日志")
    @PostMapping
    public ResponseEntity<Integer> createUserLog(@RequestBody UserLog userLog) {
        return ResponseEntity.ok(userLogService.createUserLog(userLog));
    }
    
    @ApiOperation("删除日志")
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteUserLog(
            @ApiParam(value = "日志ID", required = true) @PathVariable Integer id) {
        return ResponseEntity.ok(userLogService.deleteUserLog(id));
    }
    
    @ApiOperation("更新日志")
    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateUserLog(
            @ApiParam(value = "日志ID", required = true) @PathVariable Integer id,
            @RequestBody UserLog userLog) {
        userLog.setId(id);
        return ResponseEntity.ok(userLogService.updateUserLog(userLog));
    }
    
    @ApiOperation("条件查询日志")
    @GetMapping
    public ResponseEntity<PageInfo<UserLog>> getUserLogs(
            @ApiParam("用户ID") @RequestParam(required = false) Integer userId,
            @ApiParam("开始时间") @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        
        UserLogQueryDTO queryDTO = new UserLogQueryDTO();
        queryDTO.setUserId(userId);
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);
        
        return ResponseEntity.ok(userLogService.getUserLogs(queryDTO));
    }
} 