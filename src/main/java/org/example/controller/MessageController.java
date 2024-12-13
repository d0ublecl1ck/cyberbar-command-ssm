package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.example.entity.Message;
import org.example.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Api(tags = "消息管理接口")
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    
    @Autowired
    private MessageService messageService;
    
    @ApiOperation("获取待处理消息")
    @GetMapping("/pending")
    public ResponseEntity<List<Message>> getPendingMessages() {
        return ResponseEntity.ok(messageService.getPendingMessages());
    }
    
    @ApiOperation("更新消息状态")
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, String>> updateMessageStatus(
            @ApiParam(value = "消息ID", required = true) @PathVariable Integer id,
            @ApiParam(value = "新状态", required = true) @RequestParam String status) {
        Map<String, String> response = new HashMap<>();
        try {
            boolean success = messageService.updateMessageStatus(id, status);
            if (success) {
                response.put("message", "状态更新成功");
                return ResponseEntity.ok(response);
            }
            response.put("message", "消息不存在");
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @ApiOperation("创建新消息")
    @PostMapping
    public ResponseEntity<Integer> createMessage(@RequestBody Message message) {
        return ResponseEntity.ok(messageService.createMessage(message));
    }
    
    @ApiOperation("根据用户ID获取消息")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Message>> getMessagesByUserId(
            @ApiParam(value = "用户ID", required = true) @PathVariable Integer userId) {
        return ResponseEntity.ok(messageService.getMessagesByUserId(userId));
    }
    
    @ApiOperation("根据机器ID获取消息")
    @GetMapping("/machine/{machineId}")
    public ResponseEntity<List<Message>> getMessagesByMachineId(
            @ApiParam(value = "机器ID", required = true) @PathVariable Integer machineId) {
        return ResponseEntity.ok(messageService.getMessagesByMachineId(machineId));
    }
} 