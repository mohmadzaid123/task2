package org.exalt.task2.repository;

import org.exalt.task2.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Double-booking check — derived query
    Optional<Appointment> findByDoctorIdAndAppointmentDateAndAppointmentTime(
            Long doctorId,
            LocalDate appointmentDate,
            java.time.LocalTime appointmentTime
    );

    // JPQL query
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
            "AND a.status = 'SCHEDULED' AND a.appointmentDate = :date")
    List<Appointment> findScheduledByDoctorAndDate(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date
    );

    // Native SQL query
    @Query(value = "SELECT * FROM appointments WHERE patient_id = :pid " +
            "ORDER BY appointment_date DESC LIMIT :n",
            nativeQuery = true)
    List<Appointment> findRecentByPatient(
            @Param("pid") Long patientId,
            @Param("n") int limit
    );
}
