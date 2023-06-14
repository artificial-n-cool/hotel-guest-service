package com.artificialncool.guestapp.service;


import com.artificialncool.guestapp.model.Korisnik;
import com.artificialncool.guestapp.model.Smestaj;
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

    public List<Smestaj> getAllForUser(Korisnik k) throws EntityNotFoundException
    {
        return smestajRepository
                .findByKorisnikId(k.getId())
                .orElseThrow( () -> new EntityNotFoundException("nema smestaja za korisnika"));
    }

    public List<Smestaj> getAllByNaziv(String naziv){

        return  smestajRepository.findByNazivLikeIgnoreCase(naziv);
    }

    public List<Smestaj> getAllByLokacija(String lokacija){
        return  smestajRepository.findByLokacijaLikeIgnoreCase(lokacija);
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
                .prosecnaOcena(0.0)
                .baseCena(new Cena())
                .build()
        );
    }



}
