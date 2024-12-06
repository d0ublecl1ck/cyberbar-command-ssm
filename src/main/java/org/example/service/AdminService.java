package org.example.service;

import org.example.entity.Admin;

public interface AdminService {
    Admin login(String username, String password);
} 