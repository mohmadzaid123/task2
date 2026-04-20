package org.exalt.task2.repository;

import org.exalt.task2.document.Prescription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest   // loads only MongoDB layer with embedded Mongo
class PrescriptionRepositoryTest {

    @Autowired private PrescriptionRepository prescriptionRepository;

    @Test
    void findByPatientId_returnsCorrectPrescriptions() {
        Prescription p1 = new Prescription();
        p1.setPatientId(1L);
        p1.setDoctorId(1L);
        p1.setDiagnosis("Flu");
        p1.setIssuedDate(LocalDate.now());
        prescriptionRepository.save(p1);

        Prescription p2 = new Prescription();
        p2.setPatientId(2L);   // different patient
        p2.setDoctorId(1L);
        p2.setDiagnosis("Cold");
        p2.setIssuedDate(LocalDate.now());
        prescriptionRepository.save(p2);

        List<Prescription> result =
                prescriptionRepository.findByPatientId(1L);


        assertEquals("Flu", result.get(0).getDiagnosis());
    }

    @Test
    void findByPatientIdAndIssuedDateBetween_returnsCorrectRange() {
        Prescription p = new Prescription();
        p.setPatientId(1L);
        p.setDiagnosis("Flu");
        p.setIssuedDate(LocalDate.of(2026, 3, 15));
        prescriptionRepository.save(p);

        List<Prescription> result =
                prescriptionRepository.findByPatientIdAndIssuedDateBetween(
                        1L,
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 12, 31));

        assertFalse(result.isEmpty());

        // outside the range — should return empty
        List<Prescription> empty =
                prescriptionRepository.findByPatientIdAndIssuedDateBetween(
                        1L,
                        LocalDate.of(2025, 1, 1),
                        LocalDate.of(2025, 12, 31));

        assertTrue(empty.isEmpty());
    }
}