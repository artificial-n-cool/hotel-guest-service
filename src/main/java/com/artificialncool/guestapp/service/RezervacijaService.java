package com.artificialncool.guestapp.service;


import com.artificialncool.guestapp.dto.model.RezervacijaDTO;
import com.artificialncool.guestapp.model.Rezervacija;
import com.artificialncool.guestapp.model.Smestaj;
import com.artificialncool.guestapp.model.enums.StatusRezervacije;
import com.artificialncool.guestapp.repository.SmestajRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RezervacijaService {

    private final SmestajRepository smestajRepository;
    private final SmestajService smestajService;

    public void createRezervacija(RezervacijaDTO dto) {
        Rezervacija rezervacija = Rezervacija.builder()
                .id(new ObjectId().toString())
                .brojOsoba(dto.getBrojOsoba())
                .statusRezervacije(StatusRezervacije.valueOf(dto.getStatusRezervacije()))
                .datumDo(LocalDateTime.parse(dto.getDatumDo()))
                .datumOd(LocalDateTime.parse(dto.getDatumOd()))
                .ocenjivacID(dto.getOcenjivacId())
                .build();


        Smestaj smestaj = smestajService.getById(dto.getSmestajId());

        List<Rezervacija> tempRezervacije = smestaj.getRezervacije();
        tempRezervacije.add(rezervacija);
        smestaj.setRezervacije(tempRezervacije);
        smestajService.save(smestaj);
    }


    public Rezervacija findByIdAndSmestaj(String id, String smestajId) throws EntityNotFoundException {
        Smestaj smestaj = smestajService.getById(smestajId);
        Rezervacija rezervacija
                = smestaj.getRezervacije()
                .stream()
                .filter(r -> r.getId().equals(id))
                .findFirst().orElseThrow(EntityNotFoundException::new);
        return rezervacija;
    }

    public Rezervacija setReservationStatus(String id, String smestajId, StatusRezervacije status) {
        Smestaj smestaj = smestajService.getById(smestajId);
        smestaj.setRezervacije(
                smestaj.getRezervacije()
                        .stream()
                        .peek(r -> {
                            if (r.getId().equals(id)) {
                                r.setStatusRezervacije(status);
                            }
                        })
                        .toList()
        );
        smestajService.save(smestaj);
        return findByIdAndSmestaj(id, smestajId);
    }
}
