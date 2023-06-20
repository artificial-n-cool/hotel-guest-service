package com.artificialncool.guestapp.dto.converter;

import com.artificialncool.guestapp.dto.model.RezervacijaDTO;
import com.artificialncool.guestapp.model.Rezervacija;
import com.artificialncool.guestapp.model.enums.StatusRezervacije;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RezervacijaConverter {

    public Rezervacija fromDTO(RezervacijaDTO dto)
    {
        return Rezervacija.builder()
                .id(dto.getId())
                .datumDo(LocalDateTime.parse(dto.getDatumDo()))
                .datumOd(LocalDateTime.parse(dto.getDatumOd()))
                .brojOsoba(dto.getBrojOsoba())
                .ocenjivacID(dto.getOcenjivacId())
                .statusRezervacije(StatusRezervacije.valueOf(dto.getStatusRezervacije()))
                .build();
    }

}
