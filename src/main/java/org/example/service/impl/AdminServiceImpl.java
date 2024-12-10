package org.example.service.impl;

import org.example.entity.Admin;
import org.example.mapper.AdminMapper;
import org.example.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("adminService")
public class AdminServiceImpl implements AdminService {
    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);
    
    @Autowired
    private AdminMapper adminMapper;
    
    @Override
    public Admin login(String username, String password) {
        log.info("【Service层】准备查询 - 用户名: [{}], 密码: [{}]", username, password);
        
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        
        log.info("【Service层】Admin对象 - 用户名: [{}], 密码: [{}]", admin.getUsername(), admin.getPassword());
        
        Admin result = adminMapper.login(admin);
        log.info("【Service层】查询结果: {}", result != null ? "找到匹配记录" : "未找到匹配记录");
        
        return result;
    }
} 