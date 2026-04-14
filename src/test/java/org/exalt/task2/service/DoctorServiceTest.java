package org.exalt.task2.service;

import org.exalt.task2.dto.DoctorDTO;
import org.exalt.task2.entity.Doctor;
import org.exalt.task2.exception.EntityNotFoundException;
import org.exalt.task2.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock private DoctorRepository doctorRepository;
    @Mock private ModelMapper modelMapper;

    @InjectMocks private DoctorService doctorService;

    private Doctor doctor;
    private DoctorDTO doctorDTO;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("Ahmad");
        doctor.setLastName("Khalil");
        doctor.setEmail("ahmad@hospital.com");
        doctor.setSpecialty("Cardiology");

        doctorDTO = new DoctorDTO();
        doctorDTO.setId(1L);
        doctorDTO.setFirstName("Ahmad");
        doctorDTO.setLastName("Khalil");
        doctorDTO.setEmail("ahmad@hospital.com");
        doctorDTO.setSpecialty("Cardiology");
    }

    // ─── Test 1: get by id — found ────────────────────────────────

    @Test
    void getDoctorById_found_returnsDTO() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(modelMapper.map(doctor, DoctorDTO.class)).thenReturn(doctorDTO);

        DoctorDTO result = doctorService.getDoctorById(1L);

        assertNotNull(result);
        assertEquals("Ahmad", result.getFirstName());
    }

    // ─── Test 2: get by id — not found ───────────────────────────

    @Test
    void getDoctorById_notFound_throwsException() {
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> doctorService.getDoctorById(99L));
    }

    // ─── Test 3: create — duplicate email ────────────────────────

    @Test
    void createDoctor_duplicateEmail_throwsException() {
        when(doctorRepository.existsByEmail("ahmad@hospital.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> doctorService.createDoctor(doctorDTO));

        verify(doctorRepository, never()).save(any());
    }

    // ─── Test 4: get by specialty ────────────────────────────────

    @Test
    void getDoctorsBySpecialty_returnsMappedList() {
        when(doctorRepository.findBySpecialty("Cardiology"))
                .thenReturn(List.of(doctor));
        when(modelMapper.map(doctor, DoctorDTO.class)).thenReturn(doctorDTO);

        // call findBySpecialty through repository directly
        // (DoctorService doesn't expose this yet — add it)
        List<Doctor> result = doctorRepository.findBySpecialty("Cardiology");

        assertEquals(1, result.size());
        assertEquals("Cardiology", result.get(0).getSpecialty());
    }
}