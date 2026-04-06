package org.exalt.task2.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents a prescription stored in MongoDB.
 */
@Document(collection = "prescriptions")
@Data
public class Prescription {

    @Id
    private String id;

    private Long appointmentId;   // links back to MySQL appointment
    private Long patientId;       // links back to MySQL patient
    private Long doctorId;        // links back to MySQL doctor

    private LocalDate issuedDate;
    private String diagnosis;
    private List<String> medications;
    private String notes;
}