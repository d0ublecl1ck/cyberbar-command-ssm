package org.example.mapper;

import org.example.entity.UserLog;
import org.example.dto.UserLogQueryDTO;
import java.util.List;

public interface UserLogMapper {
    int insert(UserLog userLog);
    int deleteById(Integer id);
    int update(UserLog userLog);
    List<UserLog> selectByCondition(UserLogQueryDTO queryDTO);
} 