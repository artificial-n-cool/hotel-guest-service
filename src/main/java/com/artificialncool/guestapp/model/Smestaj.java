package com.artificialncool.guestapp.model;

import com.artificialncool.guestapp.model.helpers.Cena;
import com.artificialncool.guestapp.model.helpers.OcenaSmestaja;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
@Document("smestaji")
public class Smestaj {
    @Id
    private String id;

    private String naziv;
    private String lokacija;
    private String pogodnosti;
    private String opis;
    private Integer minGostiju;
    private Integer maxGostiju;
    private Double prosecnaOcena;
    private String vlasnikID;
    private List<OcenaSmestaja> ocene;
    private List<Promocija> promocije;
    private List<Rezervacija> rezervacije;

    private Cena baseCena;
}
