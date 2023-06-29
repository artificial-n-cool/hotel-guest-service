package com.artificialncool.guestapp.dto.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcenaSmestajaDTO {

    private String id;
    private String ocenjivacId;
    private Double ocena;
    private String datum;
    private String smestajId;
}
