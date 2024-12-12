package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.example.dto.UserLoginDTO;
import org.example.dto.UserLoginResponse;
import org.example.dto.UserQueryDTO;
import org.example.entity.User;
import org.example.entity.UserLog;
import org.example.service.UserService;
import org.example.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.UUID;

@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserLogService userLogService;
    
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
    public ResponseEntity<PageInfo<User>> searchUsers(
            @ApiParam("关键字") @RequestParam(required = false) String keyword,
            @ApiParam("用户状态") @RequestParam(required = false) String status,
            @ApiParam("最小余额") @RequestParam(required = false) BigDecimal minBalance,
            @ApiParam("最大余额") @RequestParam(required = false) BigDecimal maxBalance,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setKeyword(keyword);
        queryDTO.setStatus(status);
        queryDTO.setMinBalance(minBalance);
        queryDTO.setMaxBalance(maxBalance);
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);
        
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userService.getUsersByCondition(queryDTO);
        
        return ResponseEntity.ok(new PageInfo<>(users));
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
    
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginDTO loginDTO) {
        // 参数校验
        if ((loginDTO.getIdentityCard() == null || loginDTO.getIdentityCard().isEmpty()) 
            && (loginDTO.getPhoneNumber() == null || loginDTO.getPhoneNumber().isEmpty())) {
            return ResponseEntity.badRequest()
                .body(UserLoginResponse.error("请提供身份证号或手机号"));
        }
        
        if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(UserLoginResponse.error("密码不能为空"));
        }

        if (loginDTO.getMachineId() == null) {
            return ResponseEntity.badRequest()
                .body(UserLoginResponse.error("请选择上机设备"));
        }
        
        // 尝试登录
        User user = userService.login(loginDTO);
        
        if (user != null) {
            try {
                // 登录成功后直接上机
                userService.startUsingComputer(user.getId(), loginDTO.getMachineId());
                
                // 生成token（这里使用简单的UUID，实际项目中应该使用JWT）
                String token = UUID.randomUUID().toString();
                
                // 记录登录日志
                UserLog userLog = new UserLog();
                userLog.setUserId(user.getId());
                userLog.setOperation("LOGIN");
                userLog.setDetail("用户登录成功并上机，机器ID: " + loginDTO.getMachineId());
                userLogService.createUserLog(userLog);
                
                return ResponseEntity.ok(UserLoginResponse.success(token, user));
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest()
                    .body(UserLoginResponse.error(e.getMessage()));
            }
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(UserLoginResponse.error("用户名或密码错误"));
    }
    
    @ApiOperation("用户上机")
    @PostMapping("/{userId}/start/{machineId}")
    public ResponseEntity<Map<String, Object>> startUsingComputer(
            @ApiParam(value = "用户ID", required = true) @PathVariable Integer userId,
            @ApiParam(value = "机器ID", required = true) @PathVariable Integer machineId) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.startUsingComputer(userId, machineId);
            
            // 记录上机日志
            UserLog userLog = new UserLog();
            userLog.setUserId(userId);
            userLog.setOperation("START_USING");
            userLog.setDetail("开始使用机器 " + machineId);
            userLogService.createUserLog(userLog);
            
            response.put("message", "上机成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @ApiOperation("用户下机")
    @PostMapping("/{userId}/stop")
    public ResponseEntity<Map<String, Object>> stopUsingComputer(
            @ApiParam(value = "用户ID", required = true) @PathVariable Integer userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.stopUsingComputer(userId);
            
            // 记录下机日志
            UserLog userLog = new UserLog();
            userLog.setUserId(userId);
            userLog.setOperation("STOP_USING");
            userLog.setDetail("结束使用机器");
            userLogService.createUserLog(userLog);
            
            response.put("message", "下机成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 