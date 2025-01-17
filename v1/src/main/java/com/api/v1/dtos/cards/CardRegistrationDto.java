package com.api.v1.dtos.cards;

import com.api.v1.annotations.SSN;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CardRegistrationDto(
        @NotNull LocalDate dueDate,
        @Size(min=3, max=3) String cvc,
        @NotBlank String ownerName,
        @SSN String ssnOwner
) {
}
