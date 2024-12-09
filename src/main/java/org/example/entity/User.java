package org.example.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;
    private String name;
    private String identityCard;
    private String phoneNumber;
    private BigDecimal balance;
    private String loginPassword;
    private String status;  // Offline, Online, Banned
    private Integer machineId;
    private LocalDateTime lastOnComputerTime;
    private LocalDateTime lastOffComputerTime;
    private LocalDateTime registerTime;
} 