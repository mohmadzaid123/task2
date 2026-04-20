package org.exalt.task2.service;

import org.exalt.task2.dto.AppointmentDTO;
import org.exalt.task2.dto.PrescriptionDTO;
import org.exalt.task2.entity.Appointment;
import org.exalt.task2.entity.Doctor;
import org.exalt.task2.entity.Patient;
import org.exalt.task2.enums.AppointmentStatus;
import org.exalt.task2.exception.DoubleBookingException;
import org.exalt.task2.exception.EntityNotFoundException;
import org.exalt.task2.repository.AppointmentRepository;
import org.exalt.task2.repository.DoctorRepository;
import org.exalt.task2.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)   // pure Mockito — no Spring context
class AppointmentServiceTest {

    @Mock private AppointmentRepository appointmentRepository;
    @Mock private DoctorRepository doctorRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private ModelMapper modelMapper;
    @Mock private PrescriptionService prescriptionService;

    @InjectMocks private AppointmentService appointmentService;

    private AppointmentDTO dto;
    private Doctor doctor;
    private Patient patient;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        dto = new AppointmentDTO();
        dto.setDoctorId(1L);
        dto.setPatientId(1L);
        dto.setAppointmentDate(LocalDate.of(2026, 5, 1));
        dto.setAppointmentTime(LocalTime.of(10, 0));

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("Ahmad");

        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("Sara");

        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setAppointmentTime(dto.getAppointmentTime());
    }

    // ─── Test 1: successful booking ───────────────────────────────

    @Test
    void bookAppointment_success() {
        // no existing appointment at that slot
        when(appointmentRepository
                .findByDoctorIdAndAppointmentDateAndAppointmentTime(1L,
                        dto.getAppointmentDate(), dto.getAppointmentTime()))
                .thenReturn(Optional.empty());

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(appointmentRepository.save(any())).thenReturn(appointment);
        when(modelMapper.map(appointment, AppointmentDTO.class)).thenReturn(dto);

        AppointmentDTO result = appointmentService.bookAppointment(dto);

        assertNotNull(result);
        verify(appointmentRepository, times(1)).save(any()); //times(1) for save
    }

    // ─── Test 2: double booking ───────────────────────────────────

    @Test
    void bookAppointment_doubleBooking_throwsException() {
        // existing appointment found at same slot
        when(appointmentRepository
                .findByDoctorIdAndAppointmentDateAndAppointmentTime(1L,
                        dto.getAppointmentDate(), dto.getAppointmentTime()))
                .thenReturn(Optional.of(appointment));

        assertThrows(DoubleBookingException.class,
                () -> appointmentService.bookAppointment(dto));

        // save should NEVER be called
        verify(appointmentRepository, never()).save(any());
    }

    // ─── Test 3: cancel — appointment not found ───────────────────

    @Test
    void cancelAppointment_notFound_throwsException() {
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> appointmentService.cancelAppointment(99L));
    }

    // ─── Test 4: cancel — already completed ──────────────────────

    @Test
    void cancelAppointment_alreadyCompleted_throwsException() {
        appointment.setStatus(AppointmentStatus.COMPLETED);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        assertThrows(IllegalStateException.class,
                () -> appointmentService.cancelAppointment(1L));
    }

    // ─── Test 5: complete appointment ────────────────────────────

    @Test
    void completeAppointment_success() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenReturn(appointment);
        when(modelMapper.map(appointment, AppointmentDTO.class)).thenReturn(dto);

        // mock the prescription initialization  ← add this
        when(prescriptionService.initializePrescription(1L)).thenReturn(new PrescriptionDTO());

        AppointmentDTO result = appointmentService.completeAppointment(1L);

        assertNotNull(result);
        assertEquals(AppointmentStatus.COMPLETED, appointment.getStatus());
        verify(appointmentRepository, times(1)).save(appointment);
        verify(prescriptionService, times(1)).initializePrescription(1L); // ← verify it was called
    }
}