package com.artificialncool.guestapp.model.helpers;
import com.artificialncool.guestapp.model.Korisnik;
import com.artificialncool.guestapp.model.Smestaj;
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
public class OcenaSmestaja {
    @Id
    private String id;
    private Double ocena;
    private LocalDateTime datum;
    private String ocenjivacID;
}

