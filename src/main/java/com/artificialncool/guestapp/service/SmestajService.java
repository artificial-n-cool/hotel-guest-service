package com.artificialncool.guestapp.service;


import com.artificialncool.guestapp.dto.converter.SmestajConverter;
import com.artificialncool.guestapp.dto.model.SmestajDTO;
import com.artificialncool.guestapp.model.Rezervacija;
import com.artificialncool.guestapp.model.Smestaj;
import com.artificialncool.guestapp.model.enums.StatusRezervacije;
import com.artificialncool.guestapp.model.enums.TipCene;
import com.artificialncool.guestapp.model.helpers.Cena;
import com.artificialncool.guestapp.repository.SmestajRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class SmestajService {

    private final SmestajRepository smestajRepository;
    private final SmestajConverter smestajConverter;


    public void deleteSmestaj(String id) {
        smestajRepository.deleteById(id);
    }

    public Smestaj fromDTO(SmestajDTO dto) {
        return smestajConverter.fromDTO(dto);
    }

    public SmestajDTO toDTO(Smestaj smestaj) {
        return smestajConverter.toDTO(smestaj);
    }

    public List<Smestaj> getAllSmestaj() {
        return smestajRepository.findAll();
    }

    public Smestaj getById(String id) throws EntityNotFoundException {
        return smestajRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No such smestaj"));
    }

    public List<Smestaj> getAllByNaziv(String naziv) {
        return smestajRepository.findByNazivContainsIgnoreCase(naziv);
    }

    public List<Smestaj> getAllByLokacija(String lokacija) {
        return smestajRepository.findByLokacijaContainsIgnoreCase(lokacija);
    }


    public List<Smestaj> getByNazivAndLokacija(String naziv, String lokacija) {
        return smestajRepository.findByNazivContainsIgnoreCaseAndLokacijaContainsIgnoreCase(naziv, lokacija);
    }

    public List<Smestaj> getAllAboveAverage(Double criterium) {
        return smestajRepository.findByProsecnaOcenaGreaterThanEqual(criterium);
    }

    public List<Smestaj> getAllByVlasnikID(String vlasnikID) {
        return smestajRepository.findByVlasnikID(vlasnikID);
    }


    public boolean checkIfOverlap(Rezervacija r1, Rezervacija r2) {
        boolean overlap = r1.getDatumOd().isBefore(r2.getDatumDo())
                && r1.getDatumDo().isAfter(r2.getDatumOd());

        return overlap;
    }

    public Smestaj save(Smestaj smestaj) {
        return smestajRepository.save(smestaj);
    }

    public Smestaj copy(Smestaj old, Smestaj updated) {
        old.setNaziv(updated.getNaziv());
        old.setLokacija(updated.getLokacija());
        old.setPogodnosti(updated.getPogodnosti());
        old.setOpis(updated.getOpis());
        old.setMinGostiju(updated.getMinGostiju());
        old.setMaxGostiju(updated.getMaxGostiju());
        old.setBaseCena(updated.getBaseCena());

        return old;
    }

    public Smestaj update(Smestaj updated) throws EntityNotFoundException {
        Smestaj old = getById(updated.getId());

        old = copy(old, updated);

        return smestajRepository.save(old);
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
                .vlasnikID("1")
                .prosecnaOcena(0.0)
                .baseCena(Cena.builder().cena(50.0).tipCene(TipCene.PO_OSOBI).build())
                .build()
        );
        smestajRepository.save(Smestaj.builder()
                .naziv("Soko apartmani")
                .lokacija("Avenija Neka 56")
                .opis("Jako dobar smestaj i sve super")
                .maxGostiju(5)
                .minGostiju(2)
                .pogodnosti("Fenomenalne")
                .vlasnikID("2")
                .prosecnaOcena(5.0)
                .baseCena(Cena.builder().cena(120.0).tipCene(TipCene.PO_SMESTAJU).build())
                .build()
        );
    }


    public Page<Smestaj> read(String location, Integer numGuests, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return smestajRepository.findAllByLocationAndGuestsAndNoReservationsBetween(location.trim().toLowerCase(), numGuests,
                from, to, pageable);
    }

    public boolean checkIfCanGrade(String ocenjivacId, String hostId) {
        List<Smestaj> smestaji = smestajRepository.findByVlasnikID(hostId);
        for (var smestaj :
                smestaji) {
            for (var res :
                    smestaj.getRezervacije()) {
                if (res.getOcenjivacID().equals(ocenjivacId) && res.getDatumDo().isBefore(LocalDateTime.now()) && res.getStatusRezervacije().equals(StatusRezervacije.PRIHVACENO)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkIfCanGradeSmestaj(String ocenjivacId, String smestajId) {
        var smestaj = this.getById(smestajId);
        if (smestaj.getRezervacije() == null){
            return false;
        }
        for (var res :
                smestaj.getRezervacije()) {
            if (res.getOcenjivacID().equals(ocenjivacId) && res.getDatumDo().isBefore(LocalDateTime.now()) && res.getStatusRezervacije().equals(StatusRezervacije.PRIHVACENO)) {
                return true;
            }
        }

        return false;
    }
}
