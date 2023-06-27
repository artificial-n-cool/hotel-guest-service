package com.artificialncool.guestapp.model.helpers;


import com.artificialncool.guestapp.model.enums.TipCene;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Cena {
    @Id
    private String id;
    private Double cena;
    private TipCene tipCene;
}
