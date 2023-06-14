package com.artificialncool.guestapp.repository;

import com.artificialncool.guestapp.model.Korisnik;
import com.artificialncool.guestapp.model.helpers.OcenaKorisnika;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface KorisnikRepository extends MongoRepository<Korisnik, String> {




}
