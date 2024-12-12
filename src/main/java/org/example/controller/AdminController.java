package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.example.dto.LoginResponse;
import org.example.entity.Admin;
import org.example.service.AdminService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.example.util.JwtUtil;

@Api(tags = "管理员接口")
@RestController
@RequestMapping(value = "/api/admin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminController {
    private static final Logger log = LogManager.getLogger(AdminController.class);
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @ApiOperation("管理员登录")
    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> login(
            @ApiParam(value = "用户名", required = true) @RequestParam String username,
            @ApiParam(value = "密码", required = true) @RequestParam String password,
            HttpServletRequest request) {
        log.info("【请求信息】Method: {}", request.getMethod());
        log.info("【请求信息】ContentType: {}", request.getContentType());
        log.info("【登录请求】原始参数 - 用户名: [{}], 密码: [{}]", username, password);
        
        // 参数检查
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            log.error("【登录失败】用户名或密码为空");
            return ResponseEntity.badRequest().body(LoginResponse.error("用户名和密码不能为空"));
        }
        
        // 去除可能的空格
        username = username.trim();
        password = password.trim();
        log.info("【登录请求】处理后参数 - 用户名: [{}], 密码: [{}]", username, password);
        
        Admin admin = adminService.login(username, password);
        if (admin != null) {
            String token = jwtUtil.generateAdminToken(admin.getUsername(), admin.getId());
            log.info("【登录成功】用户名: {}", username);
            return ResponseEntity.ok(LoginResponse.success(token, admin));
        }
        
        log.error("【登录失败】用户名: {}", username);
        return ResponseEntity.badRequest().body(LoginResponse.error("用户名或密码错误"));
    }
} 