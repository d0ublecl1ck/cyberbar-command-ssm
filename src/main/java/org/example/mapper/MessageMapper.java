package org.example.mapper;

import org.example.entity.Message;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MessageMapper {
    List<Message> selectPendingMessages();
    int updateStatus(@Param("id") Integer id, @Param("status") String status);
    Message selectById(Integer id);
    int insert(Message message);
    List<Message> selectByUserId(Integer userId);
    List<Message> selectByMachineId(Integer machineId);
} 