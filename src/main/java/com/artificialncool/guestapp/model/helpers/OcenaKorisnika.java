package com.artificialncool.guestapp.model.helpers;

import jakarta.persistence.Embedded;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OcenaKorisnika {
    @Id
    private String id;
    private Double ocena;
    private String ocenjivacID;
    private LocalDateTime datum;
}

