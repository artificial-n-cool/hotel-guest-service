package com.artificialncool.guestapp.controller;

import com.artificialncool.guestapp.dto.model.RezervacijaDTO;
import com.artificialncool.guestapp.model.enums.StatusRezervacije;
import com.artificialncool.guestapp.service.RezervacijaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/guest/rezervacija")
@RequiredArgsConstructor
public class RezervacijaController {


    private final RezervacijaService rezervacijaService;
    @PostMapping(value = "/addRezervacija")
    public ResponseEntity<Void> addRezervacija(@RequestBody RezervacijaDTO dto) {
        rezervacijaService.createRezervacija(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/setStatusRezervacije/{id}/{smestajId}/{status}")
    public ResponseEntity<Void> setStatusRezervacije(@PathVariable String id, @PathVariable String smestajID, @PathVariable String status){
        StatusRezervacije sta = StatusRezervacije.valueOf(status);
        rezervacijaService.setReservationStatus(id, smestajID, sta);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
