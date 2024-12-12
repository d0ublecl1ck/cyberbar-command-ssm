package org.example.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String identityCard;
    private String phoneNumber;
    private String password;
    private Integer machineId;
} 