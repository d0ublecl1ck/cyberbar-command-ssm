package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.example.dto.UserQueryDTO;
import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @ApiOperation("创建新用户")
    @PostMapping
    public ResponseEntity<Integer> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }
    
    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }
    
    @ApiOperation("更新用户信息")
    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateUser(
            @PathVariable Integer id,
            @RequestBody User user) {
        user.setId(id);
        return ResponseEntity.ok(userService.updateUser(user));
    }
    
    @ApiOperation("搜索用户")
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @ApiParam("关键字") @RequestParam(required = false) String keyword,
            @ApiParam("用户状态") @RequestParam(required = false) String status,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setKeyword(keyword);
        queryDTO.setStatus(status);
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);
        
        return ResponseEntity.ok(userService.getUsersByCondition(queryDTO));
    }
    
    @ApiOperation("获取用户统计信息")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUserStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userService.getTotalUsersCount());
        stats.put("onlineUsers", userService.getCountByStatus("Online"));
        stats.put("offlineUsers", userService.getCountByStatus("Offline"));
        stats.put("bannedUsers", userService.getCountByStatus("Banned"));
        return ResponseEntity.ok(stats);
    }
    
    @ApiOperation("获取所有用户")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @ApiOperation("根据ID获取用户")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
} 