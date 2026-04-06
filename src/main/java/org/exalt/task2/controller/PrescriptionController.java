package org.exalt.task2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.exalt.task2.dto.PrescriptionDTO;
import org.exalt.task2.service.PrescriptionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    // GET /api/prescriptions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDTO> getPrescriptionById(@PathVariable String id) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    // POST /api/prescriptions
    @PostMapping
    public ResponseEntity<PrescriptionDTO> addPrescription(
            @Valid @RequestBody PrescriptionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(prescriptionService.addPrescription(dto));
    }

    // GET /api/prescriptions/patient/1
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionDTO>> getPatientPrescriptions(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(prescriptionService.getPatientPrescriptions(patientId));
    }

    // GET /api/prescriptions/patient/1/range?start=2026-01-01&end=2026-12-31
    @GetMapping("/patient/{patientId}/range")
    public ResponseEntity<List<PrescriptionDTO>> getByDateRange(
            @PathVariable Long patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(
                prescriptionService.getPatientPrescriptionsByDateRange(patientId, start, end));
    }
}