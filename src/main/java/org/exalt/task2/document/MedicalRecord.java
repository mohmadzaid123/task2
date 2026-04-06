package org.exalt.task2.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

/**
 * Represents a medical record stored in MongoDB.
 */
@Document(collection = "medical_records")
@Data
public class MedicalRecord {

    @Id
    private String id;

    private Long patientId;       // links back to MySQL patient

    private LocalDate recordDate;
    private String recordType;    // e.g. "LAB_RESULT", "XRAY", "CHECKUP"
    private String description;
    private String result;
}