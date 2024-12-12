package org.example.service;

import org.example.entity.User;
import org.example.entity.User.UserStatus;
import org.example.dto.UserQueryDTO;
import org.example.dto.UserLoginDTO;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Integer id);
    int createUser(User user);
    int updateUser(User user);
    int deleteUser(Integer id);
    int getTotalUsersCount();
    int getCountByStatus(String status);
    List<User> getUsersByCondition(UserQueryDTO queryDTO);
    User login(UserLoginDTO loginDTO);
    
    /**
     * 用户上机
     * @param userId 用户ID
     * @param machineId 机器ID
     * @return 操作结果
     */
    int startUsingComputer(Integer userId, Integer machineId);
    
    /**
     * 用户下机
     * @param userId 用户ID
     * @return 操作结果
     */
    int stopUsingComputer(Integer userId);
    
    /**
     * 根据状态获取用户列表
     */
    List<User> getUsersByStatus(UserStatus status);
} 