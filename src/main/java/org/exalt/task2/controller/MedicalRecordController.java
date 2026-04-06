package org.exalt.task2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.exalt.task2.dto.MedicalRecordDTO;
import org.exalt.task2.service.MedicalRecordService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    // POST /api/medical-records
    @PostMapping
    public ResponseEntity<MedicalRecordDTO> addMedicalRecord(
            @Valid @RequestBody MedicalRecordDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medicalRecordService.addMedicalRecord(dto));
    }

    // GET /api/medical-records/patient/1
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordDTO>> getPatientRecords(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getPatientRecords(patientId));
    }

    // GET /api/medical-records/patient/1/range?start=2026-01-01&end=2026-12-31
    @GetMapping("/patient/{patientId}/range")
    public ResponseEntity<List<MedicalRecordDTO>> getByDateRange(
            @PathVariable Long patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(
                medicalRecordService.getPatientRecordsByDateRange(patientId, start, end));
    }

    // GET /api/medical-records/patient/1/type?type=XRAY
    @GetMapping("/patient/{patientId}/type")
    public ResponseEntity<List<MedicalRecordDTO>> getByType(
            @PathVariable Long patientId,
            @RequestParam String type) {
        return ResponseEntity.ok(
                medicalRecordService.getPatientRecordsByType(patientId, type));
    }
}