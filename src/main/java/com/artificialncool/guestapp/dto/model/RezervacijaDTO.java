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
    private String datumOd;
    private String datumDo;
    private String statusRezervacije;
    private String ocenjivacId;
    private String smestajId;

}
