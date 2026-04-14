package org.exalt.task2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.exalt.task2.dto.DoctorDTO;
import org.exalt.task2.exception.EntityNotFoundException;
import org.exalt.task2.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)   // loads only web layer
class DoctorControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private DoctorService doctorService;

    // ─── Test 1: create doctor — returns 201 ──────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")   // simulate logged-in admin
    void createDoctor_returns201() throws Exception {
        DoctorDTO dto = new DoctorDTO();
        dto.setFirstName("Ahmad");
        dto.setLastName("Khalil");
        dto.setEmail("ahmad@hospital.com");
        dto.setLicenseNumber("LIC-001");

        when(doctorService.createDoctor(any())).thenReturn(dto);

        mockMvc.perform(post("/api/doctors")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Ahmad"))
                .andExpect(jsonPath("$.email").value("ahmad@hospital.com"));
    }

    // ─── Test 2: get doctor — not found returns 404 ───────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    void getDoctor_notFound_returns404() throws Exception {
        when(doctorService.getDoctorById(99L))
                .thenThrow(new EntityNotFoundException("Doctor not found with id: 99"));

        mockMvc.perform(get("/api/doctors/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Doctor not found with id: 99"));
    }

    // ─── Test 3: create doctor — invalid body returns 400 ─────────

    @Test
    @WithMockUser(roles = "ADMIN")
    void createDoctor_invalidBody_returns400() throws Exception {
        DoctorDTO dto = new DoctorDTO();
        dto.setFirstName("");        // violates @NotBlank
        dto.setEmail("not-email");  // violates @Email

        mockMvc.perform(post("/api/doctors")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}