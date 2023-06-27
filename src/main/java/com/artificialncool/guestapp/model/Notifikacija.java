package com.artificialncool.guestapp.model;

import com.artificialncool.guestapp.model.enums.TipNotifikacije;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notifikacija {
    @Id
    private String id;
    private TipNotifikacije tipNotifikacije;
    private String tekst;
}
