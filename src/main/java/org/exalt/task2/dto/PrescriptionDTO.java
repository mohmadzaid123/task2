package org.exalt.task2.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PrescriptionDTO {

    private String id;

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    private LocalDate issuedDate;
    private String diagnosis;
    private List<String> medications;
    private String notes;
}