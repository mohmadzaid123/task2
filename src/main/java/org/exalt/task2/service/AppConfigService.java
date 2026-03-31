package org.exalt.task2.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")   // its the default but i add it for clarify
public class AppConfigService {
    public String getHospitalName() {
        return "Smart Hospital";
    }
}