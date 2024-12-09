package org.example.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.dto.UserLogQueryDTO;
import org.example.entity.UserLog;
import org.example.mapper.UserLogMapper;
import org.example.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLogServiceImpl implements UserLogService {
    
    @Autowired
    private UserLogMapper userLogMapper;
    
    @Override
    public int createUserLog(UserLog userLog) {
        return userLogMapper.insert(userLog);
    }
    
    @Override
    public int deleteUserLog(Integer id) {
        return userLogMapper.deleteById(id);
    }
    
    @Override
    public int updateUserLog(UserLog userLog) {
        return userLogMapper.update(userLog);
    }
    
    @Override
    public PageInfo<UserLog> getUserLogs(UserLogQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        return new PageInfo<>(userLogMapper.selectByCondition(queryDTO));
    }
} 