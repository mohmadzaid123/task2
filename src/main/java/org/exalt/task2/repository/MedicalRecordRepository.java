package org.exalt.task2.repository;

import org.exalt.task2.document.MedicalRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends MongoRepository<MedicalRecord, String> {

    // all records for a patient
    List<MedicalRecord> findByPatientId(Long patientId);

    // date range
    List<MedicalRecord> findByPatientIdAndRecordDateBetween(
            Long patientId,
            LocalDate startDate,
            LocalDate endDate
    );

    // by type
    List<MedicalRecord> findByPatientIdAndRecordType(Long patientId, String recordType);
}