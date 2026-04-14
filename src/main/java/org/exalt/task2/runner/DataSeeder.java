package org.exalt.task2.runner;

import lombok.RequiredArgsConstructor;
import org.exalt.task2.entity.UserAccount;
import org.exalt.task2.enums.UserRole;
import org.exalt.task2.repository.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(2)   // runs after HospitalBootstrapRunner
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // only seed if no users exist

        System.out.println("User count:"+ userAccountRepository.count());
        if (userAccountRepository.count() > 0) return;

        createUser("admin",   "admin123",   UserRole.ADMIN);
        createUser("doctor1", "doctor123",  UserRole.DOCTOR);
        createUser("patient1","patient123", UserRole.PATIENT);

        System.out.println("=== Test users seeded ===");
        System.out.println("  admin   / admin123");
        System.out.println("  doctor1 / doctor123");
        System.out.println("  patient1/ patient123");
    }

    private void createUser(String username, String password, UserRole role) {
        UserAccount account = new UserAccount();
        account.setUsername(username);
        account.setPasswordHash(passwordEncoder.encode(password));
        account.setRole(role);
        account.setEnabled(true);
        userAccountRepository.save(account);
    }
}