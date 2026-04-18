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
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Appointment not found: " + dto.getAppointmentId()));

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Prescription can only be added to a COMPLETED appointment");
        }

        // find existing skeleton and update it
        List<Prescription> existing =
                prescriptionRepository.findByAppointmentId(dto.getAppointmentId());

        Prescription prescription = existing.isEmpty()
                ? new Prescription()       // fallback if no skeleton
                : existing.get(0);         // update existing skeleton

        // fill in the details the doctor provides
        prescription.setAppointmentId(dto.getAppointmentId());
        prescription.setDoctorId(appointment.getDoctor().getId());
        prescription.setPatientId(appointment.getPatient().getId());
        prescription.setIssuedDate(LocalDate.now());
        prescription.setDiagnosis(dto.getDiagnosis());
        prescription.setMedications(dto.getMedications());
        prescription.setNotes(dto.getNotes());

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

    /**
     * Called automatically when an appointment is marked COMPLETED.
     * Creates a skeleton prescription in MongoDB so the doctor
     * can fill in the details later.
     */
    public PrescriptionDTO initializePrescription(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Appointment not found: " + appointmentId));

        // avoid creating duplicate skeleton if one already exists
        List<Prescription> existing =
                prescriptionRepository.findByAppointmentId(appointmentId);
        if (!existing.isEmpty()) {
            return modelMapper.map(existing.get(0), PrescriptionDTO.class);
        }

        // create skeleton — empty fields, doctor fills later
        Prescription skeleton = new Prescription();
        skeleton.setAppointmentId(appointmentId);
        skeleton.setDoctorId(appointment.getDoctor().getId());
        skeleton.setPatientId(appointment.getPatient().getId());
        skeleton.setIssuedDate(LocalDate.now());
        skeleton.setDiagnosis("");           // empty — doctor fills later
        skeleton.setMedications(List.of()); // empty — doctor fills later
        skeleton.setNotes("Pending — please complete prescription details");

        Prescription saved = prescriptionRepository.save(skeleton);

        System.out.println("=== Skeleton prescription created in MongoDB " +
                "for appointment: " + appointmentId + " ===");

        return modelMapper.map(saved, PrescriptionDTO.class);
    }



}