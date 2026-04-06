package org.exalt.task2.repository;

import org.exalt.task2.document.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {

    // derived query — find all prescriptions for a patient
    List<Prescription> findByPatientId(Long patientId);

    // derived query — find by doctor
    List<Prescription> findByDoctorId(Long doctorId);

    // derived query — find by appointment
    List<Prescription> findByAppointmentId(Long appointmentId);

    // date range query
    @Query("{ 'patientId': ?0, 'issuedDate': { $gte: ?1, $lte: ?2 } }")
    List<Prescription> findByPatientIdAndIssuedDateBetween(
            Long patientId,
            LocalDate startDate,
            LocalDate endDate
    );
}