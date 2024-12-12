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
    User loginByIdentityCard(@Param("identityCard") String identityCard, @Param("password") String password);
    User loginByPhoneNumber(@Param("phoneNumber") String phoneNumber, @Param("password") String password);
    /**
     * 根据状态查询用户
     */
    List<User> selectByStatus(@Param("status") String status);
} 