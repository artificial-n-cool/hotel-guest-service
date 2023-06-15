package com.artificialncool.guestapp.controller;


import com.artificialncool.guestapp.dto.converter.SmestajConverter;
import com.artificialncool.guestapp.dto.model.OcenaSmestajaDTO;
import com.artificialncool.guestapp.dto.model.SmestajDTO;
import com.artificialncool.guestapp.model.Rezervacija;
import com.artificialncool.guestapp.model.Smestaj;
import com.artificialncool.guestapp.model.helpers.OcenaSmestaja;
import com.artificialncool.guestapp.service.OcenaSmestajService;
import com.artificialncool.guestapp.service.SmestajService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/guest/smestaj")
@RequiredArgsConstructor
public class SmestajController {
    private final SmestajService smestajService;
    private final OcenaSmestajService ocenaSmestajService;
    private final SmestajConverter smestajConverter;


    @GetMapping
    public ResponseEntity<List<SmestajDTO>> readAll(){
        return new ResponseEntity<>(
          smestajService.getAllSmestaj()
                  .stream().map(smestajService::toDTO).toList(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{naziv}/{lokacija}/{datumOd}/{datumDo}")
    public ResponseEntity<List<SmestajDTO>> findByNazivAndLokacija(@PathVariable String naziv, @PathVariable String lokacija, @PathVariable LocalDate datumOd, @PathVariable LocalDate datumDo){
        Rezervacija dummyRez = Rezervacija.builder().datumDo(datumDo).datumOd(datumOd).build();
        return new ResponseEntity<>(
                smestajService.getByNazivAndLokacija(naziv, lokacija)
                .stream().filter(smestaj -> smestaj.getRezervacije().stream().noneMatch(rezervacija -> smestajService.checkIfOverlap(rezervacija, dummyRez)))
                .map(smestajService::toDTO).toList()
                ,HttpStatus.OK
        );
    }


    @PutMapping(value = "/oceni")
    public ResponseEntity<SmestajDTO> oceniSmestaj(@RequestBody OcenaSmestajaDTO ocenaSmestajaDTO) throws EntityNotFoundException{
        try{
            OcenaSmestaja novaOcena = ocenaSmestajService.fromDTO(ocenaSmestajaDTO);
            Smestaj smestaj = smestajService.getById(ocenaSmestajaDTO.getSmestajId());
            List<OcenaSmestaja> prethodneOcene = smestaj.getOcene();
            Double stariProsek = smestaj.getProsecnaOcena();
            Double noviProsek = 0.0;
            if ( smestaj.getOcene().toArray().length != 0 )
            {
                noviProsek = ((stariProsek * smestaj.getOcene().toArray().length) + novaOcena.getOcena()) / (smestaj.getOcene().toArray().length + 1);
            }

            prethodneOcene.add(novaOcena);
            smestaj.setOcene(prethodneOcene);
            smestaj.setProsecnaOcena(noviProsek);
            Smestaj s = smestajService.save(smestaj);
            return new ResponseEntity<>(smestajService.toDTO(s), HttpStatus.OK);

        }
        catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nema ovog smestaja!");
        }
    }

    @PutMapping(value = "/otkaziRezervacije")
    public ResponseEntity<Void> otkaziRezervaciju(@RequestBody String smestajID, @RequestBody String rezervacijaID) throws EntityNotFoundException{
        try{
            Smestaj smestaj = smestajService.getById(smestajID);
            List<Rezervacija> sveRezervacije = smestaj.getRezervacije();
            sveRezervacije.removeIf(rezervacija -> rezervacija.getId().equals(rezervacijaID));
            smestaj.setRezervacije(sveRezervacije);
            smestajService.save(smestaj);
            return new ResponseEntity<>(HttpStatus.OK);

        }
        catch (EntityNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Smestaj nije pronadjen", ex);
        }
    }
}
