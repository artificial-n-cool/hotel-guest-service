package com.artificialncool.guestapp.model.helpers;

import com.artificialncool.guestapp.model.Korisnik;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

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

