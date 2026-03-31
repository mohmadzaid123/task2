package org.exalt.task2.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.exalt.task2.enums.UserRole;

/**
 * Represents a user account linked to a patient or doctor.
 */
@Entity
@Table(name = "user_accounts")
@Data
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    private boolean enabled = true;

    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}