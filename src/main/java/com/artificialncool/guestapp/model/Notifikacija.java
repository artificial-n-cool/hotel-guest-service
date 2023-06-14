package com.artificialncool.guestapp.model;

import com.artificialncool.guestapp.model.enums.TipNotifikacije;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("notifikacije")
public class Notifikacija {
    @Id
    private String id;

    private TipNotifikacije tipNotifikacije;

    private Korisnik primalac;

    @DocumentReference
    private String tekst;
}
