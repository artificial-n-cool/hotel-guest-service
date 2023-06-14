package com.artificialncool.guestapp.model;

import com.artificialncool.guestapp.model.enums.KorisnickaUloga;
import com.artificialncool.guestapp.model.helpers.OcenaKorisnika;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
@Document("korisnici")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Korisnik {
    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Indexed(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    private String ime;
    private String prezime;

    @Indexed(unique = true)
    private String email;

    private List<OcenaKorisnika> ocene;

    private List<Notifikacija> notifikacije;

    private String prebivalste;
    private Double prosecnaOcena;
    private KorisnickaUloga uloga;
}
