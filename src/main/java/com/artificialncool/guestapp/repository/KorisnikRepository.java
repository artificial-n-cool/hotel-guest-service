package com.artificialncool.guestapp.repository;

import com.artificialncool.guestapp.model.Korisnik;
import com.artificialncool.guestapp.model.enums.KorisnickaUloga;
import com.artificialncool.guestapp.model.helpers.OcenaKorisnika;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface KorisnikRepository extends MongoRepository<Korisnik, String> {
    Optional<Korisnik> findByUsername(String username);

    Optional<Korisnik> findByEmailIgnoreCase(String email);

    List<Korisnik> findAllByUloga(KorisnickaUloga uloga);

    List<Korisnik> findByProsecnaOcenaGreaterThanEqual(Double prosecnaOcena);

    long deleteByUsername(String username);

    long deleteByEmailIgnoreCase(String email);

    Page<Korisnik> findAllByImeContainingAndUloga(String ime, KorisnickaUloga uloga, Pageable pageable);

    @Query(value = "{ 'id' : ?0 }", fields = "{'ocene': 1}")
    Page<OcenaKorisnika> findOceneForKorisnik(String hostId, Pageable pageable);

//    Page<OcenaKorisnika> findOceneById(String hostId, Pageable pageable);

    Page<OcenaKorisnika> findOceneKorisnikaById(String hostId, Pageable pageable);

    @Query(value = "{'id': ?0}")
    Korisnik getById(String ocenjivacId);
}
