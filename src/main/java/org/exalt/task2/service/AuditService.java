package org.exalt.task2.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    @PostConstruct
    public void init() {
        // Called automatically after Spring creates this bean
        System.out.println("=== AuditService: Loading audit configuration on startup ===");
    }

    @PreDestroy
    public void destroy() {
        // Called automatically before Spring shuts down this bean
        System.out.println("=== AuditService: Flushing pending audit logs on shutdown ===");
    }
}