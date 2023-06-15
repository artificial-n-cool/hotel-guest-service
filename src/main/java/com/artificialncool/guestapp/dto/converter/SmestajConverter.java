package com.artificialncool.guestapp.dto.converter;


import com.artificialncool.guestapp.dto.model.SmestajDTO;
import com.artificialncool.guestapp.model.Smestaj;
import com.artificialncool.guestapp.model.helpers.Cena;
import org.springframework.stereotype.Component;

@Component
public class SmestajConverter {
    public Smestaj fromDTO(SmestajDTO dto){
        return Smestaj.builder()
                .id(dto.getId())
                .naziv(dto.getNaziv())
                .lokacija(dto.getLokacija())
                .opis(dto.getOpis())
                .pogodnosti(dto.getPogodnosti())
                .prosecnaOcena(dto.getProsecnaOcena())
                .minGostiju(dto.getMinGostiju())
                .maxGostiju(dto.getMaxGostiju())
                .baseCena(
                        Cena.builder()
                                .cena(dto.getBaseCena())
                                .tipCene(dto.getTipCene())
                                .build()
                )
                .vlasnikID(dto.getVlasnikID())
                .build();
    }

    public SmestajDTO toDTO(Smestaj smestaj){
        return SmestajDTO.builder()
                .id(smestaj.getId())
                .naziv(smestaj.getNaziv())
                .lokacija(smestaj.getLokacija())
                .opis(smestaj.getOpis())
                .pogodnosti(smestaj.getPogodnosti())
                .prosecnaOcena(smestaj.getProsecnaOcena())
                .minGostiju(smestaj.getMinGostiju())
                .maxGostiju(smestaj.getMaxGostiju())
                .baseCena(smestaj.getBaseCena().getCena())
                .tipCene(smestaj.getBaseCena().getTipCene())
                .build();

    }

}

