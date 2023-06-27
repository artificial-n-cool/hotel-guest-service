package com.artificialncool.guestapp.model.helpers;

import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OcenaSmestaja {
    @Id
    private String id;
    private Double ocena;
    private LocalDateTime datum;
    private String ocenjivacID;
}

