package org.example.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class User {
    private Integer id;
    
    @NotNull
    private String name;
    
    @NotNull
    @Pattern(regexp = "^\\d{17}[0-9X]$")
    private String identityCard;
    
    @Pattern(regexp = "^\\d{11}$")
    private String phoneNumber;
    
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal balance = new BigDecimal("0.00");
    
    @NotNull
    private String loginPassword;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.Offline;
    
    private Integer machineId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastOnComputerTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastOffComputerTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registerTime;
    
    public enum UserStatus {
        Offline, Online, Banned
    }
} 