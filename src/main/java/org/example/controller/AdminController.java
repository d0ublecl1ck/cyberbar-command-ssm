package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.example.entity.Admin;
import org.example.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        // 打印请求方式
        System.out.println("【请求信息】Method: " + request.getMethod());
        System.out.println("【请求信息】ContentType: " + request.getContentType());
        System.out.println("【登录请求】原始参数 - 用户名: [" + username + "], 密码: [" + password + "]");
        
        // 参数检查
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.out.println("【登录失败】用户名或密码为空");
            return ResponseEntity.badRequest().body("{\"message\":\"用户名和密码不能为空\"}");
        }
        
        // 去除可能的空格
        username = username.trim();
        password = password.trim();
        System.out.println("【登录请求】处理后参数 - 用户名: [" + username + "], 密码: [" + password + "]");
        
        Admin admin = adminService.login(username, password);
        if (admin != null) {
            System.out.println("【登录成功】用户名: " + username);
            return ResponseEntity.ok().body("{\"message\":\"登录成功\"}");
        }
        System.out.println("【登录失败】用户名: " + username);
        return ResponseEntity.badRequest().body("{\"message\":\"用户名或密码错误\"}");
    }
} 