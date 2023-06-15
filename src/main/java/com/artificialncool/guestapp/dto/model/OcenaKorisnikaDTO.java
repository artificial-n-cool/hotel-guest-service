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
    private LocalDateTime datum;
    private String hostId;
}
