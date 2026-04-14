package org.exalt.task2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.exalt.task2.dto.AppointmentDTO;
import org.exalt.task2.exception.DoubleBookingException;
import org.exalt.task2.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private AppointmentService appointmentService;

    // ─── Test 1: book appointment — returns 201 ───────────────────

    @Test
    @WithMockUser(roles = "PATIENT")
    void bookAppointment_success_returns201() throws Exception {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setDoctorId(1L);
        dto.setPatientId(1L);
        dto.setAppointmentDate(LocalDate.of(2026, 5, 1));
        dto.setAppointmentTime(LocalTime.of(10, 0));

        when(appointmentService.bookAppointment(any())).thenReturn(dto);

        mockMvc.perform(post("/api/appointments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.doctorId").value(1))
                .andExpect(jsonPath("$.patientId").value(1));
    }

    // ─── Test 2: double booking — returns 409 ─────────────────────

    @Test
    @WithMockUser(roles = "PATIENT")
    void bookAppointment_doubleBooking_returns409() throws Exception {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setDoctorId(1L);
        dto.setPatientId(1L);
        dto.setAppointmentDate(LocalDate.of(2026, 5, 1));
        dto.setAppointmentTime(LocalTime.of(10, 0));

        when(appointmentService.bookAppointment(any()))
                .thenThrow(new DoubleBookingException("Doctor already booked at this time"));

        mockMvc.perform(post("/api/appointments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Doctor already booked at this time"));
    }
}