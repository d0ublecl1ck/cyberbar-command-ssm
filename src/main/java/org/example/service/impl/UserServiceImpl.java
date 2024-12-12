package org.example.service.impl;

import org.example.context.AdminContext;
import org.example.dto.UserLoginDTO;
import org.example.entity.Machine;
import org.example.entity.ManagementLog;
import org.example.entity.User;
import org.example.entity.User.UserStatus;
import org.example.dto.UserQueryDTO;
import org.example.service.MachineService;
import org.example.service.ManagementLogService;
import org.example.service.UserService;
import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MachineService machineService;
    
    @Autowired
    private ManagementLogService managementLogService;
    
    @Autowired
    private AdminContext adminContext;

    @Override
    public List<User> getAllUsers() {
        logOperation("QUERY_USER", "查询所有用户列表");
        return userMapper.selectAll();
    }

    @Override
    public User getUserById(Integer id) {
        logOperation("QUERY_USER", String.format("查询用户详情，用户ID: %d", id));
        return userMapper.selectById(id);
    }

    @Override
    public int createUser(User user) {
        int result = userMapper.insert(user);
        if (result > 0) {
            logOperation("CREATE_USER", String.format(
                "创建新用户，用户名: %s, 身份证号: %s, 手机号: %s, 初始余额: %.2f元",
                user.getName(),
                user.getIdentityCard(),
                user.getPhoneNumber(),
                user.getBalance()
            ));
        }
        return result;
    }

    @Override
    public int updateUser(User user) {
        User currentUser = userMapper.selectById(user.getId());
        if (currentUser == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 创建更新对象，保留原有值
        User updateUser = new User();
        updateUser.setId(user.getId());
        
        // 只更新非空字段，保留原有值
        updateUser.setName(user.getName() != null ? user.getName() : currentUser.getName());
        updateUser.setPhoneNumber(user.getPhoneNumber() != null ? user.getPhoneNumber() : currentUser.getPhoneNumber());
        updateUser.setIdentityCard(user.getIdentityCard() != null ? user.getIdentityCard() : currentUser.getIdentityCard());
        updateUser.setLoginPassword(user.getLoginPassword() != null ? user.getLoginPassword() : currentUser.getLoginPassword());
        updateUser.setBalance(user.getBalance() != null ? user.getBalance() : currentUser.getBalance());
        updateUser.setStatus(user.getStatus() != null ? user.getStatus() : currentUser.getStatus());
        updateUser.setMachineId(user.getMachineId() != null ? user.getMachineId() : currentUser.getMachineId());
        updateUser.setLastOnComputerTime(user.getLastOnComputerTime() != null ? user.getLastOnComputerTime() : currentUser.getLastOnComputerTime());
        updateUser.setLastOffComputerTime(user.getLastOffComputerTime() != null ? user.getLastOffComputerTime() : currentUser.getLastOffComputerTime());
        
        int result = userMapper.update(updateUser);
        if (result > 0) {
            StringBuilder changes = new StringBuilder();
            if (user.getName() != null && !user.getName().equals(currentUser.getName())) {
                changes.append(String.format("用户名: %s → %s; ", currentUser.getName(), user.getName()));
            }
            if (user.getPhoneNumber() != null && !user.getPhoneNumber().equals(currentUser.getPhoneNumber())) {
                changes.append(String.format("手机号: %s → %s; ", currentUser.getPhoneNumber(), user.getPhoneNumber()));
            }
            if (user.getBalance() != null && !user.getBalance().equals(currentUser.getBalance())) {
                changes.append(String.format("余额: %.2f元 → %.2f元; ", currentUser.getBalance(), user.getBalance()));
            }
            if (user.getStatus() != null && !user.getStatus().equals(currentUser.getStatus())) {
                changes.append(String.format("状态: %s → %s; ", currentUser.getStatus(), user.getStatus()));
            }
            
            logOperation("UPDATE_USER", String.format(
                "更新用户信息，用户ID: %d, 更新内容: %s",
                user.getId(),
                changes.toString()
            ));
        }
        return result;
    }

    @Override
    public int deleteUser(Integer id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            int result = userMapper.deleteById(id);
            if (result > 0) {
                logOperation("DELETE_USER", String.format(
                    "删除用户，用户ID: %d, 用户名: %s, 身份证号: %s, 手机号: %s",
                    user.getId(),
                    user.getName(),
                    user.getIdentityCard(),
                    user.getPhoneNumber()
                ));
            }
            return result;
        }
        return 0;
    }

    @Override
    public int getTotalUsersCount() {
        return userMapper.countTotal();
    }

    @Override
    public int getCountByStatus(String status) {
        return userMapper.countByStatus(status);
    }

    @Override
    public List<User> getUsersByCondition(UserQueryDTO queryDTO) {
        // 检查查询逻辑
        return userMapper.selectByCondition(queryDTO);
    }

    @Override
    @Transactional
    public User login(UserLoginDTO loginDTO) {
        User user = null;
        String loginType = "";
        String loginValue = "";
        
        if (loginDTO.getIdentityCard() != null && !loginDTO.getIdentityCard().isEmpty()) {
            loginType = "身份证号";
            loginValue = loginDTO.getIdentityCard();
            user = userMapper.loginByIdentityCard(loginDTO.getIdentityCard(), loginDTO.getPassword());
        } else if (loginDTO.getPhoneNumber() != null && !loginDTO.getPhoneNumber().isEmpty()) {
            loginType = "手机号";
            loginValue = loginDTO.getPhoneNumber();
            user = userMapper.loginByPhoneNumber(loginDTO.getPhoneNumber(), loginDTO.getPassword());
        }
        
        if (user != null) {
            // 检查用户是否在其他机器上在线
            if (UserStatus.Online.equals(user.getStatus())) {
                // 记录强制下机日志
                logOperation("USER_FORCE_LOGOUT", String.format(
                    "用户在新机器登录，强制下机，用户ID: %d, 原机器ID: %d, 新机器ID: %d",
                    user.getId(),
                    user.getMachineId(),
                    loginDTO.getMachineId()
                ));
                
                // 在新机器登录前，先执行下机操作
                stopUsingComputer(user.getId());
            }
            
            logOperation("USER_LOGIN", String.format(
                "用户登录成功，用户ID: %d, 登录方式: %s, 登录凭证: %s",
                user.getId(),
                loginType,
                loginValue
            ));
        } else {
            logOperation("USER_LOGIN_ERROR", String.format(
                "用户登录失败，录方式: %s, 登录凭证: %s",
                loginType,
                loginValue
            ));
        }
        return user;
    }

    @Override
    @Transactional
    public int startUsingComputer(Integer userId, Integer machineId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            logOperation("USER_OPERATION_ERROR", String.format(
                "上机失败，用户不存在，用户ID: %d, 机器ID: %d",
                userId, machineId
            ));
            throw new RuntimeException("用户不存在");
        }
        if (UserStatus.Banned.equals(user.getStatus())) {
            logOperation("USER_OPERATION_ERROR", String.format(
                "上机失败，用户已被封禁，用户ID: %d, 用户名: %s",
                user.getId(), user.getName()
            ));
            throw new RuntimeException("用户已被封禁");
        }
        if (UserStatus.Online.equals(user.getStatus())) {
            logOperation("USER_OPERATION_ERROR", String.format(
                "上机失败，用户已在其他机器上，用户ID: %d, 当前机器ID: %d",
                user.getId(), user.getMachineId()
            ));
            throw new RuntimeException("用户已在其他机器上");
        }
        
        Machine machine = machineService.getMachineById(machineId);
        if (machine == null) {
            logOperation("USER_OPERATION_ERROR", String.format(
                "上机失败，机器不存在，用户ID: %d, 机器ID: %d",
                userId, machineId
            ));
            throw new RuntimeException("机器不存在");
        }
        
        if (machine.getPrice() == null) {
            logOperation("USER_OPERATION_ERROR", String.format(
                "上机失败，机器价格信息异常，用户ID: %d, 机器ID: %d",
                userId, machineId
            ));
            throw new RuntimeException("机器价格信息异常");
        }
        
        if (!"Idle".equals(machine.getStatus())) {
            logOperation("USER_OPERATION_ERROR", String.format(
                "上机失败，机器不可用，用户ID: %d, 机器ID: %d, 机器状态: %s",
                userId, machineId, machine.getStatus()
            ));
            throw new RuntimeException("机器不可用");
        }
        
        // 更新用户状态
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setStatus(UserStatus.Online);
        updateUser.setMachineId(machineId);
        updateUser.setLastOnComputerTime(LocalDateTime.now());
        updateUser.setBalance(user.getBalance());
        
        // 更新机器状态
        Machine updateMachine = new Machine();
        updateMachine.setId(machineId);
        updateMachine.setStatus("Occupied");
        updateMachine.setOnlineUserId(userId);
        
        machineService.updateMachine(updateMachine);
        int result = userMapper.update(updateUser);
        if (result > 0) {
            logOperation("USER_START_COMPUTER", String.format(
                "用户上机成功，用户ID: %d, 用户名: %s, 机器ID: %d, 当前余额: %.2f元",
                user.getId(),
                user.getName(),
                machineId,
                user.getBalance()
            ));
        }
        return result;
    }
    
    @Override
    @Transactional
    public int stopUsingComputer(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            logOperation("USER_OPERATION_ERROR", String.format(
                "下机失败，用户不存在，用户ID: %d",
                userId
            ));
            throw new RuntimeException("用户不存在");
        }
        if (!UserStatus.Online.equals(user.getStatus())) {
            logOperation("USER_OPERATION_ERROR", String.format(
                "下机失败，用户未在使用机器，用户ID: %d, 用户名: %s",
                user.getId(), user.getName()
            ));
            throw new RuntimeException("用户未在使用机器");
        }
        
        // 检查上机时间是否存在
        if (user.getLastOnComputerTime() == null) {
            logOperation("USER_OPERATION_ERROR", String.format(
                "下机失败，上机时间记录异常，用户ID: %d, 用户名: %s",
                user.getId(), user.getName()
            ));
            // 如果没有上机时间记录，直接更新状态
            User updateUser = new User();
            updateUser.setId(userId);
            updateUser.setStatus(UserStatus.Offline);
            updateUser.setMachineId(null);
            updateUser.setLastOffComputerTime(LocalDateTime.now());
            
            // 更新机器状态
            Machine updateMachine = new Machine();
            updateMachine.setId(user.getMachineId());
            updateMachine.setStatus("Idle");
            updateMachine.setOnlineUserId(null);
            
            machineService.updateMachine(updateMachine);
            return userMapper.update(updateUser);
        }
        
        // 计算费用并扣除余额
        LocalDateTime startTime = user.getLastOnComputerTime();
        LocalDateTime endTime = LocalDateTime.now();
        Machine machine = machineService.getMachineById(user.getMachineId());
        
        // 添加机器和价格的检查
        if (machine == null) {
            logOperation("USER_OPERATION_ERROR", String.format(
                "下机失败，机器信息不存在，用户ID: %d, 机器ID: %d",
                user.getId(), user.getMachineId()
            ));
            throw new RuntimeException("机器信息不存在");
        }
        
        if (machine.getPrice() == null) {
            logOperation("USER_OPERATION_ERROR", String.format(
                "下机失败，机器价格信息异常，用户ID: %d, 机器ID: %d",
                user.getId(), user.getMachineId()
            ));
            throw new RuntimeException("机器价格信息异常");
        }
        
        long hours = ChronoUnit.HOURS.between(startTime, endTime);
        long minutes = ChronoUnit.MINUTES.between(startTime, endTime) % 60;
        BigDecimal totalHours = BigDecimal.valueOf(hours)
            .add(BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP));
        BigDecimal cost = totalHours.multiply(BigDecimal.valueOf(machine.getPrice()));
        
        if (user.getBalance().compareTo(cost) < 0) {
            logOperation("USER_OPERATION_ERROR", String.format(
                "下机失败，余额不足，用户ID: %d, 用户名: %s, 当前余额: %.2f元, 需付费用: %.2f元",
                user.getId(), user.getName(), user.getBalance(), cost
            ));
            throw new RuntimeException("余额不足");
        }
        
        // 更新用户状态
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setStatus(UserStatus.Offline);
        updateUser.setMachineId(null);
        updateUser.setLastOffComputerTime(endTime);
        updateUser.setBalance(user.getBalance().subtract(cost));
        
        // 更��机器状态
        Machine updateMachine = new Machine();
        updateMachine.setId(user.getMachineId());
        updateMachine.setStatus("Idle");
        updateMachine.setOnlineUserId(null);
        
        machineService.updateMachine(updateMachine);
        int result = userMapper.update(updateUser);
        if (result > 0) {
            logOperation("USER_STOP_COMPUTER", String.format(
                "用户下机成功，用户ID: %d, 用户名: %s, 机器ID: %d, 使用时长: %d小时%d分钟, 消费金额: %.2f元, 剩余余额: %.2f元",
                user.getId(),
                user.getName(),
                user.getMachineId(),
                hours,
                minutes,
                cost,
                updateUser.getBalance()
            ));
        }
        return result;
    }

    @Override
    public List<User> getUsersByStatus(UserStatus status) {
        return userMapper.selectByStatus(status.name());
    }

    private void logOperation(String operation, String detail) {
        try {
            ManagementLog log = new ManagementLog();
            log.setAdminId(adminContext.getCurrentAdminId());
            log.setOperation(operation);
            log.setDetail(detail);
            managementLogService.createManagementLog(log);
        } catch (Exception e) {
            logger.error("记录管理日志失败", e);
        }
    }
} 