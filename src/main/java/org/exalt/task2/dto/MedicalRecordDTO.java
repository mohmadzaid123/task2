package org.exalt.task2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class MedicalRecordDTO {

    private String id;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    private LocalDate recordDate;

    @NotBlank(message = "Record type is required")
    private String recordType;

    private String description;
    private String result;
}