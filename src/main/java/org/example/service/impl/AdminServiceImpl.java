package org.example.service.impl;

import org.example.entity.Admin;
import org.example.mapper.AdminMapper;
import org.example.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    
    @Override
    public Admin login(String username, String password) {
        System.out.println("【Service层】准备查询 - 用户名: [" + username + "], 密码: [" + password + "]");
        
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        
        System.out.println("【Service层】Admin对象 - 用户名: [" + admin.getUsername() + "], 密码: [" + admin.getPassword() + "]");
        
        Admin result = adminMapper.login(admin);
        System.out.println("【Service层】查询结果: " + (result != null ? "找到匹配记录" : "未找到匹配记录"));
        
        return result;
    }
} 