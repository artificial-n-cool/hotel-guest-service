package com.artificialncool.guestapp.dto.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ObrisiOcenuHostRequestDTO {
    private String ocenjivacId;
    private String hostId;
}
