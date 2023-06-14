package com.artificialncool.guestapp.repository;

import com.artificialncool.guestapp.model.Korisnik;
import com.artificialncool.guestapp.model.Smestaj;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SmestajRepository extends MongoRepository<Smestaj, String> {
    //List<Smestaj> findByNazivIgnoreCase(String naziv);

    //List<Smestaj> findByLokacijaIgnoreCase(String lokacija);

    List<Smestaj> findByNazivContainsIgnoreCase(String naziv);

    List<Smestaj> findByLokacijaContainsIgnoreCase(String lokacija);

    List<Smestaj> findByProsecnaOcenaGreaterThanEqual(Double prosecnaOcena);

    List<Smestaj> findByVlasnikID(String vlasnikID);

    long deleteByNaziv(String naziv);

    long deleteByLokacija(String lokacija);

    long deleteByVlasnikID(String vlasnikID);












}
