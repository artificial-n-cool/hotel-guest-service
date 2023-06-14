package com.artificialncool.guestapp.repository;

import com.artificialncool.guestapp.model.Korisnik;
import com.artificialncool.guestapp.model.Smestaj;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SmestajRepository extends MongoRepository<Smestaj, String> {
    List<Smestaj> findByLokacijaLikeIgnoreCase(String lokacija);

    List<Smestaj> findByNazivLikeIgnoreCase(String naziv);

    Optional<List<Smestaj>> findByKorisnikId(String Id);

    String deleteByID(String ID);

    String deleteByNaziv(String naziv);



}
