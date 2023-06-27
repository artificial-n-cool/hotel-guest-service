package com.artificialncool.guestapp.dto.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmestajRequestDTO {
    private String location = "";

    @PositiveOrZero(message = "Upper bound for number of guests must not be negative.")
    private Integer numGuests = 0;

    @NotBlank(message = "To date must be provided.")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    private String from;

    @NotBlank(message = "To date must be provided.")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    private String to;

    public LocalDateTime getFromLocalDate() {
        return LocalDate.parse(from, DateTimeFormatter.ISO_DATE).atStartOfDay();
    }

    public LocalDateTime getToLocalDate() {
        return LocalDate.parse(to, DateTimeFormatter.ISO_DATE).atStartOfDay();
    }
}
