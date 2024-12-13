package org.example.service.impl;

import org.example.entity.Message;
import org.example.mapper.MessageMapper;
import org.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class MessageServiceImpl implements MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<Message> getPendingMessages() {
        return messageMapper.selectPendingMessages();
    }

    @Override
    public boolean updateMessageStatus(Integer id, String status) {
        if (!status.equals("Pending") && !status.equals("Finish")) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        return messageMapper.updateStatus(id, status) > 0;
    }

    @Override
    public Message getMessageById(Integer id) {
        return messageMapper.selectById(id);
    }

    @Override
    public int createMessage(Message message) {
        if (message.getStatus() == null) {
            message.setStatus("Pending");
        }
        if (message.getTime() == null) {
            message.setTime(LocalDateTime.now());
        }
        return messageMapper.insert(message);
    }

    @Override
    public List<Message> getMessagesByUserId(Integer userId) {
        return messageMapper.selectByUserId(userId);
    }

    @Override
    public List<Message> getMessagesByMachineId(Integer machineId) {
        return messageMapper.selectByMachineId(machineId);
    }
} 