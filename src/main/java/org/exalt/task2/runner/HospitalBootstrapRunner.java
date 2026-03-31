package org.exalt.task2.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HospitalBootstrapRunner implements CommandLineRunner {

    private final Environment environment;  // constructor injection — no @Autowired needed

    @Override
    public void run(String... args) {
        String[] activeProfiles = environment.getActiveProfiles();
        String profiles = activeProfiles.length > 0
                ? String.join(", ", activeProfiles)
                : "default";

        System.out.println("====================================");
        System.out.println("  Hospital Name : Smart Hospital");
        System.out.println("  Version       : 1.0.0");
        System.out.println("  Active Profile: " + profiles);
        System.out.println("====================================");
    }
}
