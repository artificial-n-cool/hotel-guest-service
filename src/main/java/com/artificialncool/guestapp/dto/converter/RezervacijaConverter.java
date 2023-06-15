package com.artificialncool.guestapp.dto.converter;

import com.artificialncool.guestapp.dto.model.RezervacijaDTO;
import com.artificialncool.guestapp.model.Rezervacija;
import com.artificialncool.guestapp.model.enums.StatusRezervacije;
import org.springframework.stereotype.Component;

@Component
public class RezervacijaConverter {

    public Rezervacija fromDTO(RezervacijaDTO dto)
    {
        return Rezervacija.builder()
                .id(dto.getId())
                .datumDo(dto.getDatumDo())
                .datumOd(dto.getDatumOd())
                .brojOsoba(dto.getBrojOsoba())
                .ocenjivacID(dto.getOcenjivacId())
                .statusRezervacije(StatusRezervacije.valueOf(dto.getStatusRezervacije()))
                .build();
    }

}
