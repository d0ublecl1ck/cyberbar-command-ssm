package org.example.service.impl;

import org.example.entity.Machine;
import org.example.entity.User;
import org.example.entity.User.UserStatus;
import org.example.entity.UserLog;
import org.example.service.MachineService;
import org.example.service.UserLogService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class UserMonitorServiceImpl {
    @Autowired
    private UserService userService;

    @Autowired
    private MachineService machineService;

    @Autowired
    private UserLogService userLogService;

    /**
     * 每分钟检查一次用户余额
     */
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkUserBalance() {
        // 获取所有在线用户
        List<User> onlineUsers = userService.getUsersByStatus(UserStatus.Online);

        for (User user : onlineUsers) {
            Machine machine = machineService.getMachineById(user.getMachineId());
            if (machine == null) continue;

            // 计算当前费用
            LocalDateTime startTime = user.getLastOnComputerTime();
            LocalDateTime now = LocalDateTime.now();
            long hours = ChronoUnit.HOURS.between(startTime, now);
            long minutes = ChronoUnit.MINUTES.between(startTime, now) % 60;
            BigDecimal totalHours = BigDecimal.valueOf(hours)
                    .add(BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP));
            BigDecimal cost = totalHours.multiply(BigDecimal.valueOf(machine.getPrice()));

            // 计算剩余可用时间（分钟）
            BigDecimal remainingBalance = user.getBalance().subtract(cost);
            BigDecimal hourlyRate = BigDecimal.valueOf(machine.getPrice());
            BigDecimal remainingMinutes = remainingBalance.divide(hourlyRate, 2, RoundingMode.FLOOR)
                    .multiply(BigDecimal.valueOf(60));

            // 检查余额是否不足
            if (remainingBalance.compareTo(BigDecimal.ZERO) <= 0) {
                // 余额不足，强制下机
                forceLogout(user, "余额不足，系统自动下机");
                continue;
            }

            // 检查是否需要余额预警
            if (remainingBalance.compareTo(user.getBalanceWarningThreshold()) <= 0) {
                // 记录预警日志
                UserLog warningLog = new UserLog();
                warningLog.setUserId(user.getId());
                warningLog.setOperation("BALANCE_WARNING");
                warningLog.setDetail(String.format(
                        "余额不足预警：当前余额%.2f元，预计可用时间%.0f分钟",
                        remainingBalance.doubleValue(),
                        remainingMinutes.doubleValue()
                ));
                userLogService.createUserLog(warningLog);
            }
        }
    }

    /**
     * 强制用户下机
     */
    @Transactional
    public void forceLogout(User user, String reason) {
        try {
            userService.stopUsingComputer(user.getId());

            // 记录强制下机日志
            UserLog logoutLog = new UserLog();
            logoutLog.setUserId(user.getId());
            logoutLog.setOperation("FORCE_LOGOUT");
            logoutLog.setDetail(reason);
            userLogService.createUserLog(logoutLog);
        } catch (Exception e) {
            // 记录错误日志
            UserLog errorLog = new UserLog();
            errorLog.setUserId(user.getId());
            errorLog.setOperation("ERROR");
            errorLog.setDetail("强制下机失败：" + e.getMessage());
            userLogService.createUserLog(errorLog);
        }
    }
} 