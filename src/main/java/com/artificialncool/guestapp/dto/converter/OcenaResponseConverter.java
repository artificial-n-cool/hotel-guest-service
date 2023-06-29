package com.artificialncool.guestapp.dto.converter;


import com.artificialncool.guestapp.dto.model.OcenaResponseDTO;
import com.artificialncool.guestapp.model.helpers.OcenaKorisnika;
import com.artificialncool.guestapp.model.helpers.OcenaSmestaja;
import org.springframework.stereotype.Component;

@Component
public class OcenaResponseConverter {

    public OcenaResponseDTO toDTOForOcena(OcenaKorisnika ocenaKorisnika) {
        return OcenaResponseDTO.builder()
                .ocenjivacId(ocenaKorisnika.getOcenjivacID())
                .datum(DateConverter.toString(ocenaKorisnika.getDatum()))
                .ocena(ocenaKorisnika.getOcena())
                .build();
    }

    public OcenaResponseDTO toDTOForOcena(OcenaSmestaja ocenaSmestaja) {
        return OcenaResponseDTO.builder()
                .ocenjivacId(ocenaSmestaja.getOcenjivacID())
                .datum(DateConverter.toString(ocenaSmestaja.getDatum()))
                .ocena(ocenaSmestaja.getOcena())
                .build();
    }
}
