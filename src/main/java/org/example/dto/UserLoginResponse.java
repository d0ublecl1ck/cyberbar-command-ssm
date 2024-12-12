package org.example.dto;

import lombok.Data;
import org.example.entity.User;

@Data
public class UserLoginResponse {
    private String message;
    private String token;
    private User user;
    
    public static UserLoginResponse success(String token, User user) {
        UserLoginResponse response = new UserLoginResponse();
        response.setMessage("登录成功");
        response.setToken(token);
        response.setUser(user);
        return response;
    }
    
    public static UserLoginResponse error(String message) {
        UserLoginResponse response = new UserLoginResponse();
        response.setMessage(message);
        return response;
    }
} 