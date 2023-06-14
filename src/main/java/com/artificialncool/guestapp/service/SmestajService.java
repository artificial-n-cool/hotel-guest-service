package com.artificialncool.guestapp.service;


import com.artificialncool.guestapp.model.Korisnik;
import com.artificialncool.guestapp.model.Smestaj;
import com.artificialncool.guestapp.model.enums.TipCene;
import com.artificialncool.guestapp.model.helpers.Cena;
import com.artificialncool.guestapp.repository.SmestajRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SmestajService {

    private final SmestajRepository smestajRepository;

    public List<Smestaj> getAllSmestaj(){
        return smestajRepository.findAll();
    }

    public List<Smestaj> getAllByNaziv(String naziv){
        return smestajRepository.findByNazivIgnoreCase(naziv);
    }

    public List<Smestaj> getAllByLokacija(String lokacija){
        return smestajRepository.findByLokacijaIgnoreCase(lokacija);
    }

    public List<Smestaj> getAllAboveAverage(Double criterium){
        return smestajRepository.findByProsecnaOcenaGreaterThanEqual(criterium);
    }

    public List<Smestaj> getAllByVlasnikID(String vlasnikID){
        return smestajRepository.findByVlasnikID(vlasnikID);
    }

    public void createSmestaj() {

        // API call da se uzme Korisnik po ID-u


        smestajRepository.save(Smestaj.builder()
                .naziv("Lux apartmani")
                .lokacija("Bulevar Neki 12")
                .opis("Jako dobar stan")
                .maxGostiju(4)
                .minGostiju(1)
                .pogodnosti("Ima svasta nesto")
                .vlasnikID("1")
                .prosecnaOcena(0.0)
                .baseCena(Cena.builder().cena(50.0).tipCene(TipCene.PO_OSOBI).build())
                .build()
        );
    }



}
