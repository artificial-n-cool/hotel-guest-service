package com.artificialncool.guestapp.dto.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OcenaResponseDTO {
    private String ocenjivacId;
    private String username;
    private String datum;
    private Double ocena;
}
