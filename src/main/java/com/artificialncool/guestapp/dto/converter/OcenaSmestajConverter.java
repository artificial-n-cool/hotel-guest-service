package com.artificialncool.guestapp.dto.converter;

import com.artificialncool.guestapp.dto.model.OcenaSmestajaDTO;
import com.artificialncool.guestapp.model.helpers.OcenaSmestaja;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OcenaSmestajConverter {

    public OcenaSmestaja fromDTO(OcenaSmestajaDTO dto)
    {
        return OcenaSmestaja.builder()
                .id(dto.getId())
                .datum(LocalDateTime.now())
                .ocena(dto.getOcena())
                .ocenjivacID(dto.getOcenjivacId())
                .build();
    }


}
