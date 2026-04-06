package org.exalt.task2.service;

import lombok.RequiredArgsConstructor;
import org.exalt.task2.dto.PatientDTO;
import org.exalt.task2.entity.Patient;
import org.exalt.task2.exception.EntityNotFoundException;
import org.exalt.task2.repository.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, PatientDTO.class))
                .toList();
    }

    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));
        return modelMapper.map(patient, PatientDTO.class);
    }

    public PatientDTO createPatient(PatientDTO dto) {
        Patient patient = modelMapper.map(dto, Patient.class);
        return modelMapper.map(patientRepository.save(patient), PatientDTO.class);
    }

    public PatientDTO updatePatient(Long id, PatientDTO dto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setEmail(dto.getEmail());
        patient.setPhone(dto.getPhone());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setBloodGroup(dto.getBloodGroup());
        return modelMapper.map(patientRepository.save(patient), PatientDTO.class);
    }

    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new EntityNotFoundException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }
}