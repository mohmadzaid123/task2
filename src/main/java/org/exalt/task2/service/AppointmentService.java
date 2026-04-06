package org.exalt.task2.service;

import lombok.RequiredArgsConstructor;
import org.exalt.task2.dto.AppointmentDTO;
import org.exalt.task2.entity.Appointment;
import org.exalt.task2.entity.Doctor;
import org.exalt.task2.entity.Patient;
import org.exalt.task2.enums.AppointmentStatus;
import org.exalt.task2.exception.DoubleBookingException;
import org.exalt.task2.exception.EntityNotFoundException;
import org.exalt.task2.repository.AppointmentRepository;
import org.exalt.task2.repository.DoctorRepository;
import org.exalt.task2.repository.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(a -> modelMapper.map(a, AppointmentDTO.class))
                .toList();
    }

    public AppointmentDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: " + id));
        return modelMapper.map(appointment, AppointmentDTO.class);
    }

    public AppointmentDTO bookAppointment(AppointmentDTO dto) {
        // Double-booking check
        appointmentRepository
                .findByDoctorIdAndAppointmentDateAndAppointmentTime(
                        dto.getDoctorId(),
                        dto.getAppointmentDate(),
                        dto.getAppointmentTime())
                .ifPresent(a -> {
                    throw new DoubleBookingException(
                            "Doctor already has an appointment on " +
                                    dto.getAppointmentDate() + " at " + dto.getAppointmentTime());
                });

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found: " + dto.getDoctorId()));

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found: " + dto.getPatientId()));

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        return modelMapper.map(appointmentRepository.save(appointment), AppointmentDTO.class);
    }

    public AppointmentDTO cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found: " + id));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        return modelMapper.map(appointmentRepository.save(appointment), AppointmentDTO.class);
    }

    public AppointmentDTO completeAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found: " + id));

        appointment.setStatus(AppointmentStatus.COMPLETED);
        return modelMapper.map(appointmentRepository.save(appointment), AppointmentDTO.class);
    }
}