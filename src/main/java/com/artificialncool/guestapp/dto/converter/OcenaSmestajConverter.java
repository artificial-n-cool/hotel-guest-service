package com.artificialncool.guestapp.dto.converter;

import com.artificialncool.guestapp.dto.model.OcenaSmestajaDTO;
import com.artificialncool.guestapp.model.helpers.OcenaSmestaja;
import org.springframework.stereotype.Component;

@Component
public class OcenaSmestajConverter {

    public OcenaSmestaja fromDTO(OcenaSmestajaDTO dto)
    {
        return OcenaSmestaja.builder()
                .id(dto.getId())
                .datum(dto.getDatum())
                .ocena(dto.getOcena())
                .ocenjivacID(dto.getOcenjivacId())
                .build();
    }


}
