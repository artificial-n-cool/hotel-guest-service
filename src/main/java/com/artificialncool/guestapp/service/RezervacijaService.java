package com.artificialncool.guestapp.service;


import com.artificialncool.guestapp.dto.model.CenaSaPromocijomDTO;
import com.artificialncool.guestapp.dto.model.RezervacijaDTO;
import com.artificialncool.guestapp.model.Promocija;
import com.artificialncool.guestapp.model.Rezervacija;
import com.artificialncool.guestapp.model.Smestaj;
import com.artificialncool.guestapp.model.enums.StatusRezervacije;
import jakarta.persistence.EntityNotFoundException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RezervacijaService {

    private final SmestajService smestajService;


    public RezervacijaService(SmestajService smestajService){
        this.smestajService = smestajService;
    }

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


    public boolean CheckOneDayDiff(Rezervacija rezervacija){
        LocalDateTime rezervacijaDatum = rezervacija.getDatumOd();
        LocalDateTime danas = LocalDateTime.now();

        boolean pre = rezervacijaDatum.getDayOfYear() < danas.getDayOfYear();

        int diffInDays = Math.abs(rezervacijaDatum.getDayOfYear() - danas.getDayOfYear());

        return pre && diffInDays <= 1;
    }


    public CenaSaPromocijomDTO returnUkupnaCena(RezervacijaDTO rezervacijaDTO){
            Smestaj smestaj = null;
            try {
                 smestaj = smestajService.getById(rezervacijaDTO.getSmestajId());
            }
            catch (EntityNotFoundException ex){
                ex.printStackTrace();
                return null;
            }
            List<Promocija> promocijaList = smestaj.getPromocije();
            double baseCena = smestaj.getBaseCena().getCena();
            if (promocijaList == null || promocijaList.isEmpty()){
                return CenaSaPromocijomDTO.builder().ukupnaCena(baseCena).build();
            }
            for (Promocija promocija : promocijaList){
                LocalDate pocetakPromocije = promocija.getDatumOd();
                LocalDate pocetakRezervacije = LocalDate.parse(rezervacijaDTO.getDatumOd());
                LocalDate krajPromocije = promocija.getDatumDo();
                LocalDate krajRezervacije = LocalDate.parse(rezervacijaDTO.getDatumDo());
                if (pocetakPromocije.isBefore(pocetakRezervacije) && krajPromocije.isAfter(krajRezervacije)){
                    for (LocalDate date = pocetakRezervacije; date.isBefore(krajRezervacije); date = date.plusDays(1))
                    {
                            if(promocija.getDani().contains(date.getDayOfWeek())){
                                baseCena += baseCena * (promocija.getProcenat() + 1);
                            }
                    }
                }
            }

            return CenaSaPromocijomDTO.builder().ukupnaCena(baseCena).build();
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
