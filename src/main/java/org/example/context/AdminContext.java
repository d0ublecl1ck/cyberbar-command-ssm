package org.example.context;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class AdminContext {
    private static final String ADMIN_ID_HEADER = "X-Admin-ID";
    
    /**
     * 获取当前登录的管理员ID
     */
    public Integer getCurrentAdminId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String adminId = request.getHeader(ADMIN_ID_HEADER);
            return adminId != null ? Integer.parseInt(adminId) : null;
        }
        return null;
    }
} 