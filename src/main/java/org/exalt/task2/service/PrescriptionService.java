package org.exalt.task2.service;

import lombok.RequiredArgsConstructor;
import org.exalt.task2.document.Prescription;
import org.exalt.task2.dto.PrescriptionDTO;
import org.exalt.task2.entity.Appointment;
import org.exalt.task2.enums.AppointmentStatus;
import org.exalt.task2.exception.EntityNotFoundException;
import org.exalt.task2.repository.AppointmentRepository;
import org.exalt.task2.repository.PrescriptionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;

    public PrescriptionDTO addPrescription(PrescriptionDTO dto) {
        // appointment must exist and be COMPLETED
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Appointment not found: " + dto.getAppointmentId()));

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Prescription can only be added to a COMPLETED appointment");
        }

        Prescription prescription = modelMapper.map(dto, Prescription.class);
        prescription.setIssuedDate(LocalDate.now());
        prescription.setDoctorId(appointment.getDoctor().getId());
        prescription.setPatientId(appointment.getPatient().getId());

        return modelMapper.map(
                prescriptionRepository.save(prescription),
                PrescriptionDTO.class
        );
    }

    public List<PrescriptionDTO> getPatientPrescriptions(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId)
                .stream()
                .map(p -> modelMapper.map(p, PrescriptionDTO.class))
                .toList();
    }

    public List<PrescriptionDTO> getPatientPrescriptionsByDateRange(
            Long patientId, LocalDate startDate, LocalDate endDate) {
        return prescriptionRepository
                .findByPatientIdAndIssuedDateBetween(patientId, startDate, endDate)
                .stream()
                .map(p -> modelMapper.map(p, PrescriptionDTO.class))
                .toList();
    }

    public PrescriptionDTO getPrescriptionById(String id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Prescription not found: " + id));
        return modelMapper.map(prescription, PrescriptionDTO.class);
    }
}