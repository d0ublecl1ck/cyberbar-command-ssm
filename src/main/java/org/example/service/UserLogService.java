package org.example.service;

import org.example.dto.UserLogQueryDTO;
import org.example.entity.UserLog;
import com.github.pagehelper.PageInfo;

public interface UserLogService {
    int createUserLog(UserLog userLog);
    int deleteUserLog(Integer id);
    int updateUserLog(UserLog userLog);
    PageInfo<UserLog> getUserLogs(UserLogQueryDTO queryDTO);
} 