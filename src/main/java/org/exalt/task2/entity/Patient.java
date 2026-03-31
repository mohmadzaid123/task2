package org.exalt.task2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a patient in the hospital system.
 */
@Entity
@Table(name = "patients")
@Data
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;
    private LocalDate dateOfBirth;
    private String bloodGroup;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Appointment> appointments;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
    @ToString.Exclude
    private UserAccount userAccount;
}