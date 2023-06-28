package com.artificialncool.guestapp.controller;

import com.artificialncool.guestapp.dto.converter.KorisnikConverter;
import com.artificialncool.guestapp.dto.converter.OcenaKorisnikaConverter;
import com.artificialncool.guestapp.dto.model.HostDTO;
import com.artificialncool.guestapp.dto.model.HostRequestDTO;
import com.artificialncool.guestapp.dto.model.OcenaKorisnikaDTO;
import com.artificialncool.guestapp.model.Korisnik;
import com.artificialncool.guestapp.model.helpers.OcenaKorisnika;
import com.artificialncool.guestapp.service.KorisnikService;
import com.artificialncool.guestapp.service.SmestajService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/guest/korisnik")
public class KorisnikController {

    private final KorisnikService korisnikService;

    private final SmestajService smestajService;

    private final OcenaKorisnikaConverter ocenaKorisnikaConverter;

    private final KorisnikConverter korisnikConverter;

    public KorisnikController(KorisnikService korisnikService, SmestajService smestajService, OcenaKorisnikaConverter ocenaKorisnikaConverter, KorisnikConverter korisnikConverter) {
        this.korisnikService = korisnikService;
        this.smestajService = smestajService;
        this.ocenaKorisnikaConverter = ocenaKorisnikaConverter;
        this.korisnikConverter = korisnikConverter;
    }

    @GetMapping("/{id}")
    public ResponseEntity<HostDTO> readOne(@PathVariable String id) {

        try {
            return new ResponseEntity<>(
                    korisnikService.toDTO(korisnikService.getById(id)),
                    HttpStatus.OK
            );
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nema korisnika sa tim ID", e);
        }
    }

    @GetMapping(value = "/search-hosts")
    @ResponseStatus(HttpStatus.OK)
    public Page<HostDTO> readHosts(@Valid HostRequestDTO request,
                                   @PageableDefault Pageable pageable) {
        var page = korisnikService.readHosts(request.getIme(), pageable);
        return page.map(korisnikService::toDTO);
    }

    @PutMapping(value = "/oceniHosta")
    public ResponseEntity<HostDTO> oceniHosta(@RequestBody OcenaKorisnikaDTO ocenaKorisnikaDTO) throws EntityNotFoundException {
        try {
            OcenaKorisnika novaOcena = ocenaKorisnikaConverter.fromDTO(ocenaKorisnikaDTO);
            Korisnik host = korisnikService.getById(ocenaKorisnikaDTO.getHostId());

            // Provera da li smem da ocenjujem
            boolean canRate = smestajService.checkIfCanGrade(ocenaKorisnikaDTO.getOcenjivacId(), ocenaKorisnikaDTO.getHostId());
            if (!canRate) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nema rezervacija pa nije moguce oceniti ovog korisnika");
            }

            List<OcenaKorisnika> prethodneOcene = host.getOcene();

            // provera da li azuriram ocenu ili dodajem novu
            int increment = 1;
            double difference = ocenaKorisnikaDTO.getOcena();
            if (prethodneOcene != null) {
                for (var ocena :
                        prethodneOcene) {
                    if (ocena.getOcenjivacID().equals(ocenaKorisnikaDTO.getOcenjivacId())) {
                        difference = difference - ocena.getOcena();
                        ocena.setOcena(ocenaKorisnikaDTO.getOcena());
                        ocena.setDatum(LocalDateTime.now());
                        increment = 0;
                    }
                }
            }


            Double stariProsek = host.getProsecnaOcena();
            Double noviProsek;
            if (host.getOcene() != null && host.getOcene().toArray().length != 0) {
                noviProsek = ((stariProsek * host.getOcene().toArray().length) + difference) / (host.getOcene().toArray().length + increment);
            } else {
                noviProsek = ocenaKorisnikaDTO.getOcena();
                prethodneOcene = new ArrayList<>();
            }

            if (increment == 1) {
                prethodneOcene.add(novaOcena);
            }
            host.setOcene(prethodneOcene);
            host.setProsecnaOcena(noviProsek);
            Korisnik k = korisnikService.save(host);
            // TODO: MESSAGE CALL DA IMA NOVI HOST OCENA
            return new ResponseEntity<>(korisnikConverter.toDTO(k), HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nema ovog korisnika!", ex);
        }
    }


    @PostMapping(value = "/populate")
    public ResponseEntity<Void> populateDB() {
        korisnikService.initDb();
        return ResponseEntity.ok().build();
    }
}
