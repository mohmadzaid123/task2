package org.exalt.task2.service;

import lombok.RequiredArgsConstructor;
import org.exalt.task2.dto.DoctorDTO;
import org.exalt.task2.entity.Doctor;
import org.exalt.task2.exception.EntityNotFoundException;
import org.exalt.task2.repository.DoctorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    @Cacheable("doctors")
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(d -> modelMapper.map(d, DoctorDTO.class))
                .toList();
    }

    @Cacheable(value = "doctors", key = "#id")
    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + id));
        return modelMapper.map(doctor, DoctorDTO.class);
    }

    @CacheEvict(value = "doctors", allEntries = true)
    public DoctorDTO createDoctor(DoctorDTO dto) {
        if (doctorRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
        }
        Doctor doctor = modelMapper.map(dto, Doctor.class);
        return modelMapper.map(doctorRepository.save(doctor), DoctorDTO.class);
    }

    @CacheEvict(value = "doctors", allEntries = true)
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new EntityNotFoundException("Doctor not found with id: " + id);
        }
        doctorRepository.deleteById(id);
    }
}