package com.artificialncool.guestapp.controller;


import com.artificialncool.guestapp.dto.converter.OcenaKorisnikaConverter;
import com.artificialncool.guestapp.dto.converter.RezervacijaConverter;
import com.artificialncool.guestapp.dto.converter.SmestajConverter;
import com.artificialncool.guestapp.dto.model.*;
import com.artificialncool.guestapp.model.Korisnik;
import com.artificialncool.guestapp.model.Rezervacija;
import com.artificialncool.guestapp.model.Smestaj;
import com.artificialncool.guestapp.model.enums.StatusRezervacije;
import com.artificialncool.guestapp.model.helpers.OcenaKorisnika;
import com.artificialncool.guestapp.model.helpers.OcenaSmestaja;
import com.artificialncool.guestapp.service.KorisnikService;
import com.artificialncool.guestapp.service.OcenaSmestajService;
import com.artificialncool.guestapp.service.RezervacijaService;
import com.artificialncool.guestapp.service.SmestajService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/guest/smestaj")
public class SmestajController {
    private final SmestajService smestajService;
    private final OcenaSmestajService ocenaSmestajService;
    private final SmestajConverter smestajConverter;
    private final RezervacijaConverter rezervacijaConverter;
    private final OcenaKorisnikaConverter ocenaKorisnikaConverter;
    private final KorisnikService korisnikService;

    private final RestTemplate restTemplate;

    private final RezervacijaService rezervacijaService;


    public SmestajController(RezervacijaService rezervacijaService, RestTemplateBuilder builder, SmestajService smestajService, OcenaSmestajService ocenaSmestajService, SmestajConverter smestajConverter, RezervacijaConverter rezervacijaConverter, OcenaKorisnikaConverter ocenaKorisnikaConverter, KorisnikService korisnikService) {
        this.rezervacijaService = rezervacijaService;
        this.restTemplate = builder.build();
        this.smestajService = smestajService;
        this.ocenaSmestajService = ocenaSmestajService;
        this.smestajConverter = smestajConverter;
        this.rezervacijaConverter = rezervacijaConverter;
        this.ocenaKorisnikaConverter = ocenaKorisnikaConverter;
        this.korisnikService = korisnikService;
    }


    @DeleteMapping(value = "/deleteSmestaj/{id}")
    public ResponseEntity<Void> deleteSmestaj(@PathVariable String id) {
        smestajService.deleteSmestaj(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/updateSmestaj")
    public ResponseEntity<Void> updateSmestaj(@RequestBody SmestajDTO smestajDTO) {
        this.smestajService.update(smestajConverter.fromDTO(smestajDTO));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SmestajDTO>> readAll() {
        return new ResponseEntity<>(
                smestajService.getAllSmestaj()
                        .stream().map(smestajService::toDTO).toList(),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("HELLO FROM GUEST SERVICE", HttpStatus.OK);
    }

    @GetMapping(value = "/{naziv}/{lokacija}/{datumOd}/{datumDo}")
    public ResponseEntity<List<SmestajDTO>> findByNazivAndLokacija(@PathVariable String naziv, @PathVariable String lokacija, @PathVariable LocalDateTime datumOd, @PathVariable LocalDateTime datumDo) {
        Rezervacija dummyRez = Rezervacija.builder().datumDo(datumDo).datumOd(datumOd).build();
        return new ResponseEntity<>(
                smestajService.getByNazivAndLokacija(naziv, lokacija)
                        .stream().filter(smestaj -> smestaj.getRezervacije().stream().noneMatch(rezervacija -> smestajService.checkIfOverlap(rezervacija, dummyRez)))
                        .map(smestajService::toDTO).toList()
                , HttpStatus.OK
        );
    }

    @GetMapping(value = "/search")
    @ResponseStatus(HttpStatus.OK)
    public Page<SmestajDTO> readInventoryItems(@Valid SmestajRequestDTO request,
                                               @PageableDefault Pageable pageable) {
        var page = smestajService.read(request.getLocation(), request.getNumGuests(),
                request.getFromLocalDate(), request.getToLocalDate(), pageable);
        return page.map(smestajService::toDTO);
    }


    @PutMapping(value = "/oceniSmestaj")
    public ResponseEntity<SmestajDTO> oceniSmestaj(@RequestBody OcenaSmestajaDTO ocenaSmestajaDTO) throws EntityNotFoundException {
        try {
            OcenaSmestaja novaOcena = ocenaSmestajService.fromDTO(ocenaSmestajaDTO);
            Smestaj smestaj = smestajService.getById(ocenaSmestajaDTO.getSmestajId());
            List<OcenaSmestaja> prethodneOcene = smestaj.getOcene();
            Double stariProsek = smestaj.getProsecnaOcena();
            Double noviProsek = 0.0;
            if (smestaj.getOcene().toArray().length != 0) {
                noviProsek = ((stariProsek * smestaj.getOcene().toArray().length) + novaOcena.getOcena()) / (smestaj.getOcene().toArray().length + 1);
            } else {
                noviProsek = ocenaSmestajaDTO.getOcena();
            }

            prethodneOcene = new ArrayList<OcenaSmestaja>();
            prethodneOcene.add(novaOcena);
            smestaj.setOcene(prethodneOcene);
            smestaj.setProsecnaOcena(noviProsek);
            Smestaj s = smestajService.save(smestaj);
            // MESSAGE CALL TO NOTIFICATION SERVICE DA IMA OCENA SMESTAJA
            return new ResponseEntity<>(smestajService.toDTO(s), HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nema ovog smestaja!", e);
        }
    }


    @PutMapping(value = "/oceniHosta")
    public ResponseEntity<Double> oceniHosta(@RequestBody OcenaKorisnikaDTO ocenaKorisnikaDTO) throws EntityNotFoundException {
        try {
            OcenaKorisnika novaOcena = ocenaKorisnikaConverter.fromDTO(ocenaKorisnikaDTO);
            Korisnik host = korisnikService.getById(ocenaKorisnikaDTO.getHostId());
            List<OcenaKorisnika> prethodneOcene = host.getOcene();
            Double stariProsek = host.getProsecnaOcena();
            Double noviProsek = 0.0;
            if (host.getOcene() != null && host.getOcene().toArray().length != 0) {
                noviProsek = ((stariProsek * host.getOcene().toArray().length) + novaOcena.getOcena()) / (host.getOcene().toArray().length + 1);
            } else {
                noviProsek = ocenaKorisnikaDTO.getOcena();
            }
            prethodneOcene = new ArrayList<OcenaKorisnika>();
            prethodneOcene.add(novaOcena);
            host.setOcene(prethodneOcene);
            host.setProsecnaOcena(noviProsek);
            Korisnik k = korisnikService.save(host);
            // TODO: MESSAGE CALL DA IMA NOVI HOST OCENA
            return new ResponseEntity<>(k.getProsecnaOcena(), HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nema ovog smestaja!", ex);
        }
    }

    @PostMapping(value = "/addSmestaj")
    public ResponseEntity<Void> createSmestaj(@RequestBody SmestajDTO smestajDTO) {
        smestajService.save(smestajConverter.fromDTO(smestajDTO));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/otkaziRezervaciju")
    public ResponseEntity<Void> otkaziRezervaciju(@RequestBody String smestajID, @RequestBody String rezervacijaID) throws EntityNotFoundException {
        try {
            Smestaj smestaj = smestajService.getById(smestajID);
            List<Rezervacija> sveRezervacije = smestaj.getRezervacije();
            if (sveRezervacije == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            sveRezervacije = sveRezervacije.stream().peek(rezervacija -> {
                if (rezervacija.getId().equals(rezervacijaID) && (rezervacijaService.CheckOneDayDiff(rezervacija))) {
                    rezervacija.setStatusRezervacije(StatusRezervacije.OTKAZANO);
                }
            }).toList();
            smestaj.setRezervacije(sveRezervacije);
            smestajService.save(smestaj);
            try {
                restTemplate.postForEntity(
                        String.format("http://host-app:8080/api/host/rezervacije/cancel/%s/%s", rezervacijaID, smestajID),
                        null,
                        RezervacijaDTO.class
                );

            } catch (RestClientException ex) {
                ex.printStackTrace();
                System.out.println("NEbitno");
            }
            //TODO: MESSAGE CALL DA SE OTKAZALA REZERVACIJA
            return new ResponseEntity<>(HttpStatus.OK);


        } catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Smestaj nije pronadjen", ex);
        }
    }

    @PostMapping(value = "/rezervisiSmestaj")
    public ResponseEntity<SmestajDTO> rezervisiSmestaj(@RequestBody RezervacijaDTO rezervacijaDTO) throws EntityNotFoundException {
        try {
            Smestaj smestaj = smestajService.getById(rezervacijaDTO.getSmestajId());
            List<Rezervacija> rezervacije = smestaj.getRezervacije();
            if (rezervacije == null) {
                rezervacije = new ArrayList<Rezervacija>();
            }
            rezervacije.add(rezervacijaConverter.fromDTO(rezervacijaDTO));
            smestaj.setRezervacije(rezervacije);
            smestajService.save(smestaj);
            try {
                restTemplate.postForEntity(
                        String.format("http://host-app:8080/api/host/rezervacije"),
                        rezervacijaDTO,
                        RezervacijaDTO.class
                );

            } catch (RestClientException ex) {
                ex.printStackTrace();
                System.out.println("NEbitno");
            }
            //TODO: MESSAGE CALL TO NOTIFICATION SERVICE DA IMA NOVA REZERVACIJA
            return new ResponseEntity<>(
                    smestajConverter.toDTO(smestaj),
                    HttpStatus.OK
            );
        } catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Smestaj nije pronadjen", ex);
        }

    }
}
