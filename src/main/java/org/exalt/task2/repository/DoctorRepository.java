package org.exalt.task2.repository;

import org.exalt.task2.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // Spring generates SQL from method name automatically
    List<Doctor> findBySpecialty(String specialty);

    boolean existsByEmail(String email);
}