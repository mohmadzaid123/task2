package org.exalt.task2.service;

import org.exalt.task2.document.Prescription;
import org.exalt.task2.dto.PrescriptionDTO;
import org.exalt.task2.entity.Appointment;
import org.exalt.task2.entity.Doctor;
import org.exalt.task2.entity.Patient;
import org.exalt.task2.enums.AppointmentStatus;
import org.exalt.task2.exception.EntityNotFoundException;
import org.exalt.task2.repository.AppointmentRepository;
import org.exalt.task2.repository.PrescriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrescriptionServiceTest {

    @Mock private PrescriptionRepository prescriptionRepository;
    @Mock private AppointmentRepository appointmentRepository;
    @Mock private ModelMapper modelMapper;

    @InjectMocks private PrescriptionService prescriptionService;

    private Appointment appointment;
    private PrescriptionDTO dto;
    private Prescription prescription;

    @BeforeEach
    void setUp() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);

        Patient patient = new Patient();
        patient.setId(1L);

        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.COMPLETED);

        dto = new PrescriptionDTO();
        dto.setAppointmentId(1L);
        dto.setDiagnosis("Flu");

        prescription = new Prescription();
        prescription.setId("mongo-id-123");
        prescription.setDiagnosis("Flu");
    }

    // ─── Test 1: add prescription — linked to completed appointment

    @Test
    void addPrescription_linkedToCompletedAppointment_success() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(modelMapper.map(dto, Prescription.class)).thenReturn(prescription);
        when(prescriptionRepository.save(any())).thenReturn(prescription);
        when(modelMapper.map(prescription, PrescriptionDTO.class)).thenReturn(dto);

        PrescriptionDTO result = prescriptionService.addPrescription(dto);

        assertNotNull(result);
        verify(prescriptionRepository, times(1)).save(any());
    }

    // ─── Test 2: add prescription — appointment not completed ─────

    @Test
    void addPrescription_appointmentNotCompleted_throwsException() {
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        assertThrows(IllegalStateException.class,
                () -> prescriptionService.addPrescription(dto));

        verify(prescriptionRepository, never()).save(any());
    }

    // ─── Test 3: get patient prescriptions ───────────────────────

    @Test
    void getPatientPrescriptions_returnsMappedList() {
        when(prescriptionRepository.findByPatientId(1L))
                .thenReturn(List.of(prescription));
        when(modelMapper.map(prescription, PrescriptionDTO.class)).thenReturn(dto);

        List<PrescriptionDTO> result =
                prescriptionService.getPatientPrescriptions(1L);

        assertEquals(1, result.size());
        verify(prescriptionRepository, times(1)).findByPatientId(1L);
    }
}