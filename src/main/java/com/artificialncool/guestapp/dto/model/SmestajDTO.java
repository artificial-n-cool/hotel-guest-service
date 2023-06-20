package com.artificialncool.guestapp.dto.model;


import com.artificialncool.guestapp.model.enums.TipCene;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SmestajDTO {

    private String id;
    private String naziv;
    private String lokacija;
    private String pogodnosti;
    private String opis;
    private Integer minGostiju;
    private Integer maxGostiju;
    private Double prosecnaOcena;
    private String vlasnikID;
    private Double baseCena;
    private TipCene tipCene;
    private Double finalCena;
}
