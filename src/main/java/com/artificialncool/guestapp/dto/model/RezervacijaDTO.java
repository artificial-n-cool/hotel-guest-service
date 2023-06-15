package com.artificialncool.guestapp.dto.model;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RezervacijaDTO {
    // id
    private String id;
    private Integer brojOsoba;
    private LocalDate datumOd;
    private LocalDate datumDo;
    private String statusRezervacije;
    private String ocenjivacId;
    private String smestajId;

}
