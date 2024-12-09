package org.example.dto;

import lombok.Data;
import org.example.entity.Admin;

@Data
public class LoginResponse {
    private String message;
    private String token;
    private Admin admin;
    
    public static LoginResponse success(String token, Admin admin) {
        LoginResponse response = new LoginResponse();
        response.setMessage("登录成功");
        response.setToken(token);
        response.setAdmin(admin);
        return response;
    }
    
    public static LoginResponse error(String message) {
        LoginResponse response = new LoginResponse();
        response.setMessage(message);
        return response;
    }
} 