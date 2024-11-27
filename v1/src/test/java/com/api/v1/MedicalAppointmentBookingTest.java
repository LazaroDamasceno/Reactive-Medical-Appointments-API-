package com.api.v1;

import com.api.v1.medical_appointment.dtos.MedicalAppointmentBookingDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MedicalAppointmentBookingTest {

    @Autowired
    private WebTestClient webTestClient;

    MedicalAppointmentBookingDto bookingDto1 = new MedicalAppointmentBookingDto(
            "123456789",
            "12345678CA",
            LocalDateTime.parse("2024-12-12T12:00:00")
    );

    @Test
    @Order(1)
    void testSuccessful() {
        webTestClient
                .post()
                .uri("api/v1/medical-appointments")
                .bodyValue(bookingDto1)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    @Order(2)
    void testUnSuccessfulForDuplicatedBookingDate() {
        webTestClient
                .post()
                .uri("api/v1/medical-appointments")
                .bodyValue(bookingDto1)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    MedicalAppointmentBookingDto bookingDto2 = new MedicalAppointmentBookingDto(
            "123456789",
            "12345678CA",
            LocalDateTime.parse("2024-11-27T12:00:00")
    );

    @Test
    @Order(3)
    void testUnSuccessfulForNotAllowedBookingDate() {
        webTestClient
                .post()
                .uri("api/v1/medical-appointments")
                .bodyValue(bookingDto2)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    MedicalAppointmentBookingDto bookingDto3 = new MedicalAppointmentBookingDto(
            "123456788",
            "12345678CA",
            LocalDateTime.parse("2024-11-27T12:00:00")
    );

    @Test
    @Order(4)
    void testUnSuccessfulForNotFoundCustomer() {
        webTestClient
                .post()
                .uri("api/v1/medical-appointments")
                .bodyValue(bookingDto3)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    MedicalAppointmentBookingDto bookingDto4 = new MedicalAppointmentBookingDto(
            "123456788",
            "12345678CA",
            LocalDateTime.parse("2024-11-27T12:00:00")
    );

    @Test
    @Order(5)
    void testUnSuccessfulForNotFoundDoctor() {
        webTestClient
                .post()
                .uri("api/v1/medical-appointments")
                .bodyValue(bookingDto4)
                .exchange()
                .expectStatus().is5xxServerError();
    }

}