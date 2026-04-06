package org.exalt.task2.service;

import lombok.RequiredArgsConstructor;
import org.exalt.task2.document.MedicalRecord;
import org.exalt.task2.dto.MedicalRecordDTO;
import org.exalt.task2.exception.EntityNotFoundException;
import org.exalt.task2.repository.MedicalRecordRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final ModelMapper modelMapper;

    public MedicalRecordDTO addMedicalRecord(MedicalRecordDTO dto) {
        MedicalRecord record = modelMapper.map(dto, MedicalRecord.class);
        record.setRecordDate(LocalDate.now());
        return modelMapper.map(
                medicalRecordRepository.save(record),
                MedicalRecordDTO.class
        );
    }

    public List<MedicalRecordDTO> getPatientRecords(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId)
                .stream()
                .map(r -> modelMapper.map(r, MedicalRecordDTO.class))
                .toList();
    }

    public List<MedicalRecordDTO> getPatientRecordsByDateRange(
            Long patientId, LocalDate startDate, LocalDate endDate) {
        return medicalRecordRepository
                .findByPatientIdAndRecordDateBetween(patientId, startDate, endDate)
                .stream()
                .map(r -> modelMapper.map(r, MedicalRecordDTO.class))
                .toList();
    }

    public List<MedicalRecordDTO> getPatientRecordsByType(Long patientId, String type) {
        return medicalRecordRepository
                .findByPatientIdAndRecordType(patientId, type)
                .stream()
                .map(r -> modelMapper.map(r, MedicalRecordDTO.class))
                .toList();
    }
}