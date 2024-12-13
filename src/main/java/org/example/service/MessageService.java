package org.example.service;

import org.example.entity.Message;
import java.util.List;

public interface MessageService {
    List<Message> getPendingMessages();
    boolean updateMessageStatus(Integer id, String status);
    Message getMessageById(Integer id);
    int createMessage(Message message);
    List<Message> getMessagesByUserId(Integer userId);
    List<Message> getMessagesByMachineId(Integer machineId);
} 