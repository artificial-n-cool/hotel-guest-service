package com.artificialncool.guestapp.repository;

import com.artificialncool.guestapp.model.Korisnik;
import com.artificialncool.guestapp.model.enums.KorisnickaUloga;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface KorisnikRepository extends MongoRepository<Korisnik, String> {
    Optional<Korisnik> findByUsername(String username);

    Optional<Korisnik> findByEmailIgnoreCase(String email);

    List<Korisnik> findAllByUloga(KorisnickaUloga uloga);

    List<Korisnik> findByProsecnaOcenaGreaterThanEqual(Double prosecnaOcena);

    long deleteByUsername(String username);

    long deleteByEmailIgnoreCase(String email);




















}
