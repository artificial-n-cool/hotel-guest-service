package com.artificialncool.guestapp.dto.converter;

import com.artificialncool.guestapp.dto.model.OcenaKorisnikaDTO;
import com.artificialncool.guestapp.model.helpers.OcenaKorisnika;
import org.springframework.stereotype.Component;

@Component
public class OcenaKorisnikaConverter {


    public OcenaKorisnika fromDTO(OcenaKorisnikaDTO dto)
    {
        return OcenaKorisnika.builder()
                .ocena(dto.getOcena())
                .id(dto.getId())
                .ocenjivacID(dto.getOcenjivacId())
                .datum(dto.getDatum())
                .build();
    }
}
