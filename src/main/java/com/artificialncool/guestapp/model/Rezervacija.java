package com.artificialncool.guestapp.model;

import com.artificialncool.guestapp.model.enums.StatusRezervacije;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Rezervacija {
    @Id
    private String id;
    private Integer brojOsoba;
    private LocalDateTime datumOd;
    private LocalDateTime datumDo;
    private StatusRezervacije statusRezervacije;
    private String ocenjivacID;
}
