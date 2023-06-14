package com.artificialncool.guestapp.repository;

import com.artificialncool.guestapp.model.Korisnik;
import com.artificialncool.guestapp.model.Smestaj;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SmestajRepository extends MongoRepository<Smestaj, String> {

    Optional<Smestaj> findByNaziv(String naziv);
    Optional<Smestaj> findByLokacija(String lokacija);

    Optional<List<Smestaj>> findByKorisnik(Korisnik k);

    String deleteByID(String ID);

    String deleteByNaziv(String naziv);



}
