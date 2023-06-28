package com.artificialncool.guestapp.controller;

import com.artificialncool.guestapp.dto.model.HostDTO;
import com.artificialncool.guestapp.dto.model.HostRequestDTO;
import com.artificialncool.guestapp.service.KorisnikService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/guest/korisnik")
public class KorisnikController {

    private final KorisnikService korisnikService;

    public KorisnikController(KorisnikService korisnikService) {
        this.korisnikService = korisnikService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<HostDTO> readOne(@PathVariable String id) {

        try {
            return new ResponseEntity<>(
                    korisnikService.toDTO(korisnikService.getById(id)),
                    HttpStatus.OK
            );
        }
        catch (EntityNotFoundException e) {
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

    @PostMapping(value = "/populate")
    public ResponseEntity<Void> populateDB() {
        korisnikService.initDb();
        return ResponseEntity.ok().build();
    }
}
