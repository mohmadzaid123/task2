package org.exalt.task2.repository;

import org.exalt.task2.entity.Appointment;
import org.exalt.task2.entity.Doctor;
import org.exalt.task2.entity.Patient;
import org.exalt.task2.enums.AppointmentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest   // loads only JPA layer with H2 in-memory DB
class AppointmentRepositoryTest {

    @Autowired private TestEntityManager entityManager;
    @Autowired private AppointmentRepository appointmentRepository;

    @Test
    void findByDoctorIdAndDateAndTime_returnsExistingAppointment() {
        // seed data
        Doctor doctor = new Doctor();
        doctor.setFirstName("Ahmad");
        doctor.setLastName("Khalil");
        doctor.setEmail("ahmad@test.com");
        doctor.setLicenseNumber("LIC-TEST-001");
        entityManager.persist(doctor);

        Patient patient = new Patient();
        patient.setFirstName("Sara");
        patient.setLastName("Hassan");
        patient.setEmail("sara@test.com");
        entityManager.persist(patient);

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(LocalDate.of(2026, 5, 1));
        appointment.setAppointmentTime(LocalTime.of(10, 0));
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        entityManager.persist(appointment);
        entityManager.flush();

        // test double-booking query
        Optional<Appointment> found = appointmentRepository
                .findByDoctorIdAndAppointmentDateAndAppointmentTime(
                        doctor.getId(),
                        LocalDate.of(2026, 5, 1),
                        LocalTime.of(10, 0));

        assertTrue(found.isPresent());
        assertEquals(AppointmentStatus.SCHEDULED, found.get().getStatus());
    }

    @Test
    void findByDoctorIdAndDateAndTime_noMatch_returnsEmpty() {
        Optional<Appointment> found = appointmentRepository
                .findByDoctorIdAndAppointmentDateAndAppointmentTime(
                        999L,
                        LocalDate.of(2026, 5, 1),
                        LocalTime.of(10, 0));

        assertTrue(found.isEmpty());
    }
}