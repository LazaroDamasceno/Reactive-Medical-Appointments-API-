package com.api.v1.medical_appointments.internal.exceptions;

public class NonExistentMedicalAppointmentException extends RuntimeException {
    public NonExistentMedicalAppointmentException() {
        super("Medical appointment was not found.");
    }
}