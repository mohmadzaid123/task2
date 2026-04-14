package org.exalt.task2.repository;

import org.exalt.task2.entity.Doctor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DoctorRepositoryTest {

    @Autowired private TestEntityManager entityManager;
    @Autowired private DoctorRepository doctorRepository;

    @Test
    void findBySpecialty_returnsOnlyMatchingDoctors() {
        Doctor cardio = new Doctor();
        cardio.setFirstName("Ahmad");
        cardio.setLastName("Khalil");
        cardio.setEmail("ahmad@test.com");
        cardio.setLicenseNumber("LIC-001");
        cardio.setSpecialty("Cardiology");
        entityManager.persist(cardio);

        Doctor neuro = new Doctor();
        neuro.setFirstName("Sara");
        neuro.setLastName("Omar");
        neuro.setEmail("sara@test.com");
        neuro.setLicenseNumber("LIC-002");
        neuro.setSpecialty("Neurology");
        entityManager.persist(neuro);

        entityManager.flush();

        List<Doctor> result = doctorRepository.findBySpecialty("Cardiology");

        assertEquals(1, result.size());
        assertEquals("Cardiology", result.get(0).getSpecialty());
    }

    @Test
    void existsByEmail_returnsTrue_whenEmailExists() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("Test");
        doctor.setLastName("Doctor");
        doctor.setEmail("exists@test.com");
        doctor.setLicenseNumber("LIC-003");
        entityManager.persist(doctor);
        entityManager.flush();

        assertTrue(doctorRepository.existsByEmail("exists@test.com"));
        assertFalse(doctorRepository.existsByEmail("notexists@test.com"));
    }
}