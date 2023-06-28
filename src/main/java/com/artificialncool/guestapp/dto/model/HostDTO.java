package com.artificialncool.guestapp.dto.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class HostDTO {
    private String id;
    private String ime;
    private String prezime;
    private Double prosecnaOcena;
}
