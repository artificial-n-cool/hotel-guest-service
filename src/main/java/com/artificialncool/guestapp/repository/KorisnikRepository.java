package com.artificialncool.guestapp.repository;

import com.artificialncool.guestapp.model.Korisnik;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface KorisnikRepository extends MongoRepository<Korisnik, String> {
    Optional<Korisnik> findByUsername(String username);

    Optional<Korisnik> findByEmail(String email);

    String deleteByID(String ID);

    String deleteByUsername(String username);

    Korisnik findByPrebivalsteContainsIgnoreCase(String prebivaliste);
}
