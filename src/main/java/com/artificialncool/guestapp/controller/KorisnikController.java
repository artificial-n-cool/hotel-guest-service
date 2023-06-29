package com.artificialncool.guestapp.controller;

import com.artificialncool.guestapp.dto.converter.KorisnikConverter;
import com.artificialncool.guestapp.dto.converter.OcenaKorisnikaConverter;
import com.artificialncool.guestapp.dto.converter.OcenaResponseConverter;
import com.artificialncool.guestapp.dto.model.*;
import com.artificialncool.guestapp.model.Korisnik;
import com.artificialncool.guestapp.model.helpers.OcenaKorisnika;
import com.artificialncool.guestapp.service.KorisnikService;
import com.artificialncool.guestapp.service.SmestajService;
import jakarta.persistence.EntityNotFoundException;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/api/guest/korisnik")
public class KorisnikController {

    private final KorisnikService korisnikService;

    private final SmestajService smestajService;

    private final OcenaKorisnikaConverter ocenaKorisnikaConverter;

    private final KorisnikConverter korisnikConverter;

    private final OcenaResponseConverter ocenaResponseConverter;
    private final RestTemplate restTemplate;

    public KorisnikController(KorisnikService korisnikService, SmestajService smestajService, OcenaKorisnikaConverter ocenaKorisnikaConverter, KorisnikConverter korisnikConverter, OcenaResponseConverter ocenaResponseConverter,  RestTemplateBuilder restTemplateBuilder) {
        this.korisnikService = korisnikService;
        this.smestajService = smestajService;
        this.ocenaKorisnikaConverter = ocenaKorisnikaConverter;
        this.korisnikConverter = korisnikConverter;
        this.ocenaResponseConverter = ocenaResponseConverter;
        this.restTemplate = restTemplateBuilder.build();
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

    @GetMapping(value = "/search-host-ratings")
    @ResponseStatus(HttpStatus.OK)
    public Page<OcenaResponseDTO> readHostRatings(@Valid OceneHostRequestDTO request,
                                                  @PageableDefault Pageable pageable) {
        var k = korisnikService.getById(request.getHostId());
        var page = korisnikService.readOcene(request.getHostId(), pageable);
        for (int i = 0; i < page.getContent().size(); i++) {
            try {
                page.getContent().get(i).setOcena(k.getOcene().get(i).getOcena());
                page.getContent().get(i).setDatum(k.getOcene().get(i).getDatum());
                page.getContent().get(i).setOcenjivacID(k.getOcene().get(i).getOcenjivacID());
            } catch (Exception e) {

            }
        }
        try {
            var ocene = page.map(ocenaResponseConverter::toDTOForOcena);
            for (var ocena :
                    ocene.getContent()) {
                try {
//                    ocena.setUsername(korisnikService.getById(ocena.getOcenjivacId()).getUsername());
                    ocena.setUsername("Kobe");
                } catch (Exception e) {

                }
            }
            return ocene;
        } catch (Exception e) {

        }
        return Page.empty();
    }


    @GetMapping(value = "/search-host-ratings-list")
    @ResponseStatus(HttpStatus.OK)
    public List<OcenaResponseDTO> readHostRatingsList(@Valid OceneHostRequestDTO request
    ) {
        var k = korisnikService.getById(request.getHostId());

        var ocene = k.getOcene().stream().map(ocenaResponseConverter::toDTOForOcena).toList();

        try{
            ParameterizedTypeReference<List<Korisnik>> responseType = new ParameterizedTypeReference<List<Korisnik>>() {};

            ResponseEntity<List<Korisnik>> response = restTemplate.exchange(
                    "http://localhost:9091/api/user/all",
                    HttpMethod.GET,
                    null,
                    responseType
            );

            List<Korisnik> korisnici = response.getBody();
            for (var o :
                    ocene) {
                var uname = korisnici.stream().filter(as->as.getId().equals(o.getOcenjivacId())).findFirst().get().getUsername();
                o.setUsername(uname);
            }
            return ocene;

        }
        catch (RestClientException ex){
            ex.printStackTrace();
        }
        return new ArrayList<>();

    }

    @PutMapping(value = "/oceniHosta")
    public ResponseEntity<HostDTO> oceniHosta(@RequestBody OcenaKorisnikaDTO ocenaKorisnikaDTO) throws EntityNotFoundException {
        try {
            OcenaKorisnika novaOcena = ocenaKorisnikaConverter.fromDTO(ocenaKorisnikaDTO);
            Korisnik host = korisnikService.getById(ocenaKorisnikaDTO.getHostId());

            // Provera da li smem da ocenjujem
            boolean canRate = smestajService.checkIfCanGrade(ocenaKorisnikaDTO.getOcenjivacId(), ocenaKorisnikaDTO.getHostId());
//            if (!canRate) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nema rezervacija pa nije moguce oceniti ovog korisnika");
//            }

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
            if (host.getOcene() != null && !host.getOcene().isEmpty()) {
                noviProsek = ((stariProsek * host.getOcene().size()) + difference) / (host.getOcene().size() + increment);
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

    @PutMapping(value = "/obrisiOcenuHosta")
    public ResponseEntity<HostDTO> obrisiOcenuHosta(@RequestBody OcenaKorisnikaDTO ocenaKorisnikaDTO) throws EntityNotFoundException {
        try {
            Korisnik host = korisnikService.getById(ocenaKorisnikaDTO.getHostId());


            List<OcenaKorisnika> prethodneOcene = host.getOcene();

            // provera da li azuriram ocenu ili dodajem novu
            int increment = 0;
            double difference = 0;
            OcenaKorisnika ocenaForDelete = null;
            if (prethodneOcene != null) {
                for (var ocena :
                        prethodneOcene) {
                    if (ocena.getOcenjivacID().equals(ocenaKorisnikaDTO.getOcenjivacId())) {
                        difference = 0 - ocena.getOcena();
                        increment = 1;
                        ocenaForDelete = ocena;
                    }
                }
            }
            if (ocenaForDelete == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nema ocene pa nije moguce obrisati ocenu ovog korisnika");
            }
            var len = prethodneOcene.size();
            prethodneOcene.remove(ocenaForDelete);

            Double stariProsek = host.getProsecnaOcena();
            Double noviProsek;
            if (host.getOcene() != null && !prethodneOcene.isEmpty()) {
                noviProsek = ((stariProsek * len) + difference) / (len - increment);
            } else {
                noviProsek = 0d;
                prethodneOcene = new ArrayList<>();
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
