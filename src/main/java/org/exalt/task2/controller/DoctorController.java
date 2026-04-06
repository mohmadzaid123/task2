package org.exalt.task2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.exalt.task2.dto.DoctorDTO;
import org.exalt.task2.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody DoctorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}