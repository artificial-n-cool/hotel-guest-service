package com.artificialncool.guestapp.dto.model;

import lombok.*;

import java.time.LocalDateTime;

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
