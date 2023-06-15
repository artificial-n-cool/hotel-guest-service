package com.artificialncool.guestapp.service;

import com.artificialncool.guestapp.dto.converter.OcenaSmestajConverter;
import com.artificialncool.guestapp.dto.model.OcenaSmestajaDTO;
import com.artificialncool.guestapp.model.helpers.OcenaSmestaja;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OcenaSmestajService {

    private final OcenaSmestajConverter ocenaSmestajConverter;

    public OcenaSmestaja fromDTO(OcenaSmestajaDTO dto){
        return ocenaSmestajConverter.fromDTO(dto);
    }

}
