package com.artificialncool.guestapp.controller;

import com.artificialncool.guestapp.dto.model.CenaSaPromocijomDTO;
import com.artificialncool.guestapp.dto.model.RezervacijaDTO;
import com.artificialncool.guestapp.dto.model.SmestajDTO;
import com.artificialncool.guestapp.model.Rezervacija;
import com.artificialncool.guestapp.model.enums.StatusRezervacije;
import com.artificialncool.guestapp.service.RezervacijaService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/api/guest/rezervacija")
public class RezervacijaController {


    private final RezervacijaService rezervacijaService;
    private final RestTemplate restTemplate;


    public RezervacijaController(RezervacijaService rezervacijaService, RestTemplateBuilder restTemplateBuilder){
        this.rezervacijaService = rezervacijaService;
        this.restTemplate = restTemplateBuilder.build();
    }


    @GetMapping(value = "/getUkupnaCena")
    public ResponseEntity<CenaSaPromocijomDTO> getUkupnaCena(@RequestBody RezervacijaDTO dto){
        return new ResponseEntity<>(
                rezervacijaService.returnUkupnaCena(dto),
                HttpStatus.OK
        );
    }


    @PostMapping(value = "/addRezervacija")
    public ResponseEntity<Void> addRezervacija(@RequestBody RezervacijaDTO dto) {
        rezervacijaService.createRezervacija(dto);
        //SEND API CALL TO HOST FOR DB SYNC
        if (dto.getStatusRezervacije().equals("U_OBRADI"))
        try{
            restTemplate.postForEntity(
                    "http://host-app:8080/api/host/rezervacije",
                    dto,
                    SmestajDTO.class
            );

        }
        catch (RestClientException ex){
            ex.printStackTrace();
            System.out.println("NEbitno");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/setStatusRezervacije/{id}/{smestajId}/{status}")
    public ResponseEntity<Void> setStatusRezervacije(@PathVariable String id, @PathVariable String smestajID, @PathVariable String status){
        StatusRezervacije sta = StatusRezervacije.valueOf(status);
        Rezervacija rezervacija = rezervacijaService.setReservationStatus(id, smestajID, sta);
        if (rezervacija.getStatusRezervacije().equals(StatusRezervacije.OTKAZANO))
            try{
                restTemplate.postForEntity(
                        String.format("http://host-app:8080/api/host/rezervacije/cancel/%s/%s", rezervacija.getId(), smestajID),
                        null,
                       RezervacijaDTO.class
                );

            }
            catch (RestClientException ex){
                ex.printStackTrace();
                System.out.println("NEbitno");
            }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
