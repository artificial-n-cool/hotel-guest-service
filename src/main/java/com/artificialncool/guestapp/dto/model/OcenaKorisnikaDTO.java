package com.artificialncool.guestapp.dto.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OcenaKorisnikaDTO {
    private String id;
    private String ocenjivacId;
    private Double ocena;
    private String datum; // y:m:d h:m
    private String hostId;
}
