package org.example.service.impl;

import org.example.entity.User;
import org.example.dto.UserQueryDTO;
import org.example.service.UserService;
import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectAll();
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public int createUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    public int updateUser(User user) {
        return userMapper.update(user);
    }

    @Override
    public int deleteUser(Integer id) {
        return userMapper.deleteById(id);
    }

    @Override
    public int getTotalUsersCount() {
        return userMapper.countTotal();
    }

    @Override
    public int getCountByStatus(String status) {
        return userMapper.countByStatus(status);
    }

    @Override
    public List<User> getUsersByCondition(UserQueryDTO queryDTO) {
        // 检查查询逻辑
        return userMapper.selectByCondition(queryDTO);
    }
} 