package org.exalt.task2.service;

import lombok.RequiredArgsConstructor;
import org.exalt.task2.entity.Doctor;
import org.exalt.task2.repository.DoctorRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    @Cacheable("doctors")
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Cacheable(value = "doctors", key = "#id")
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + id));
    }

    @CacheEvict(value = "doctors", allEntries = true)
    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @CacheEvict(value = "doctors", allEntries = true)
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
}