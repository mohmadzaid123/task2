package org.exalt.task2.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.exalt.task2.enums.AppointmentStatus;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDTO {

    private Long id;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Appointment date is required")
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required")
    private LocalTime appointmentTime;

    private AppointmentStatus status;
}