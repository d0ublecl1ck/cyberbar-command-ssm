package org.example.mapper;

import org.example.entity.User;
import org.example.dto.UserQueryDTO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface UserMapper {
    List<User> selectAll();
    User selectById(Integer id);
    int insert(User user);
    int update(User user);
    int deleteById(Integer id);
    int countTotal();
    int countByStatus(@Param("status") String status);
    List<User> selectByCondition(UserQueryDTO queryDTO);
} 