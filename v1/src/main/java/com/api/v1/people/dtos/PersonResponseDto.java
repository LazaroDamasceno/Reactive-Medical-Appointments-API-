package com.api.v1.people.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record PersonResponseDto(
        String fullNameDto,
        LocalDate birthDate,
        String ssn,
        String email,
        PersonAddressDto address,
        String phoneNumber,
        String gender
) {
}
