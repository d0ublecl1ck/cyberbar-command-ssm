package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.example.dto.LoginResponse;
import org.example.entity.Admin;
import org.example.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "管理员接口")
@RestController
@RequestMapping(value = "/api/admin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    
    @Autowired
    private AdminService adminService;
    
    @ApiOperation("管理员登录")
    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> login(
            @ApiParam(value = "用户名", required = true) @RequestParam String username,
            @ApiParam(value = "密码", required = true) @RequestParam String password,
            HttpServletRequest request) {
        logger.info("【请求信息】Method: {}", request.getMethod());
        logger.info("【请求信息】ContentType: {}", request.getContentType());
        logger.info("【登录请求】原始参数 - 用户名: [{}], 密码: [{}]", username, password);
        
        // 参数检查
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            logger.error("【登录失败】用户名或密码为空");
            return ResponseEntity.badRequest().body(LoginResponse.error("用户名和密码不能为空"));
        }
        
        // 去除可能的空格
        username = username.trim();
        password = password.trim();
        logger.info("【登录请求】处理后参数 - 用户名: [{}], 密码: [{}]", username, password);
        
        Admin admin = adminService.login(username, password);
        if (admin != null) {
            // 生成token
            String token = UUID.randomUUID().toString();
            logger.info("【登录成功】用户名: {}", username);
            return ResponseEntity.ok(LoginResponse.success(token, admin));
        }
        
        logger.error("【登录失败】用户名: {}", username);
        return ResponseEntity.badRequest().body(LoginResponse.error("用户名或密码错误"));
    }
} 