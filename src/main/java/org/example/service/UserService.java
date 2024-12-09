package org.example.service;

import org.example.entity.User;
import org.example.dto.UserQueryDTO;
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
} 