package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.example.dto.UserLogQueryDTO;
import org.example.entity.UserLog;
import org.example.service.UserLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

@Api(tags = "用户日志管理接口")
@RestController
@RequestMapping("/api/userlogs")
public class UserLogController {
    private static final Logger logger = LoggerFactory.getLogger(UserLogController.class);
    
    @Autowired
    private UserLogService userLogService;
    
    @ApiOperation("创建用户日志")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUserLog(@RequestBody UserLog userLog) {
        Map<String, Object> response = new HashMap<>();
        int result = userLogService.createUserLog(userLog);
        if (result > 0) {
            response.put("message", "创建成功");
            return ResponseEntity.ok(response);
        }
        response.put("message", "创建失败");
        return ResponseEntity.badRequest().body(response);
    }
    
    @ApiOperation("删除用户日志")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUserLog(
            @ApiParam(value = "日志ID", required = true) @PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        int result = userLogService.deleteUserLog(id);
        if (result > 0) {
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        }
        response.put("message", "日志不存在");
        return ResponseEntity.notFound().build();
    }
    
    @ApiOperation("更新用户日志")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUserLog(
            @ApiParam(value = "日志ID", required = true) @PathVariable Integer id,
            @RequestBody UserLog userLog) {
        Map<String, Object> response = new HashMap<>();
        userLog.setId(id);
        int result = userLogService.updateUserLog(userLog);
        if (result > 0) {
            response.put("message", "更新成功");
            return ResponseEntity.ok(response);
        }
        response.put("message", "日志不存在");
        return ResponseEntity.notFound().build();
    }
    
    @ApiOperation("条件查询用户日志")
    @GetMapping
    public ResponseEntity<PageInfo<UserLog>> getUserLogs(
            @ApiParam("用户ID") @RequestParam(required = false) Integer userId,
            @ApiParam("操作类型") @RequestParam(required = false) String operation,
            @ApiParam("开始时间") @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        
        logger.info("查询用户日志，条件：userId={}, operation={}, startTime={}, endTime={}, pageNum={}, pageSize={}", 
                userId, operation, startTime, endTime, pageNum, pageSize);
        
        UserLogQueryDTO queryDTO = new UserLogQueryDTO();
        queryDTO.setUserId(userId);
        queryDTO.setStartTime(startTime);
        queryDTO.setEndTime(endTime);
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);
        
        return ResponseEntity.ok(userLogService.getUserLogs(queryDTO));
    }
} 