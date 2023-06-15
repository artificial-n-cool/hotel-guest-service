package com.artificialncool.guestapp.model;

import com.artificialncool.guestapp.model.enums.StatusRezervacije;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Rezervacija {
    @Id
    private String id;
    private Integer brojOsoba;
    private LocalDate datumOd;
    private LocalDate datumDo;
    private StatusRezervacije statusRezervacije;
    private String KorisnikID;
}
