package com.artificialncool.guestapp.dto.converter;

import com.artificialncool.guestapp.dto.model.HostDTO;
import com.artificialncool.guestapp.model.Korisnik;
import org.springframework.stereotype.Component;

@Component
public class KorisnikConverter {

    public HostDTO toDTO(Korisnik k) {
        return HostDTO.builder()
                .id(k.getId())
                .ime(k.getIme())
                .prezime(k.getPrezime())
                .prosecnaOcena(k.getProsecnaOcena())
                .build();

    }
}
