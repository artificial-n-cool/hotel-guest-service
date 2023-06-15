package com.artificialncool.guestapp.service;

import com.artificialncool.guestapp.model.Korisnik;
import com.artificialncool.guestapp.model.Notifikacija;
import com.artificialncool.guestapp.model.Smestaj;
import com.artificialncool.guestapp.model.enums.KorisnickaUloga;
import com.artificialncool.guestapp.model.helpers.OcenaKorisnika;
import com.artificialncool.guestapp.repository.KorisnikRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class KorisnikService {

    private final KorisnikRepository korisnikRepository;


    public List<Korisnik> getAll() {
        return korisnikRepository.findAll();
    }

    public Korisnik getById(String id) throws EntityNotFoundException{
        return korisnikRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No such smestaj"));
    }

    public Korisnik getByUsername(String username) throws EntityNotFoundException {
       return korisnikRepository.findByUsername(username)
               .orElseThrow(() -> new EntityNotFoundException("Korisnik sa tim username ne postoji"));
    }

    public Korisnik getByEmail(String email) throws EntityNotFoundException {
        return korisnikRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new EntityNotFoundException("Korisnik sa tim email ne postoji"));
    }


    public List<Korisnik> getByUloga(KorisnickaUloga uloga) throws EntityNotFoundException {
        return korisnikRepository.findAllByUloga(uloga);
    }

    public List<Korisnik> getAllHosts() {
        return getByUloga(KorisnickaUloga.HOST);
    }

    public List<Korisnik> getAllGuests(){
        return getByUloga(KorisnickaUloga.GUEST);
    }

    public List<Korisnik> getAllAboveAverageHosts(Double rating)
    {
        return korisnikRepository.findByProsecnaOcenaGreaterThanEqual(rating);
    }

    public Korisnik save(Korisnik korisnik) {
        return korisnikRepository.save(korisnik);
    }
    public void createKorisnici() {
        korisnikRepository.save(Korisnik.builder()
                .ime("Petar")
                .prezime("Petrovic")
                .username("perce")
                .password("pass")
                .email("pera@gmail.com")
                .prebivalste("Uzice")
                .prosecnaOcena(4.55)
                .uloga(KorisnickaUloga.GUEST)
                .build()
        );

        korisnikRepository.save(Korisnik.builder()
                .ime("Nikola")
                .prezime("Nikolic")
                .username("nikolica")
                .password("pass")
                .email("nikolic@gmail.com")
                .prebivalste("Loznica")
                .prosecnaOcena(2.55)
                .uloga(KorisnickaUloga.HOST)
                .build()
        );

        korisnikRepository.save(Korisnik.builder()
                .ime("Djordje")
                .prezime("Trnavcevic")
                .username("trle")
                .password("pass")
                .email("trle@gmail.com")
                .prebivalste("Uzice")
                .prosecnaOcena(4.55)
                .uloga(KorisnickaUloga.GUEST)
                .build()
        );

        korisnikRepository.save(Korisnik.builder()
                .ime("Lazar")
                .prezime("Jugovic")
                .username("Zola")
                .password("pass")
                .email("zoki@gmail.com")
                .prebivalste("Beograd")
                .prosecnaOcena(4.55)
                .uloga(KorisnickaUloga.HOST)
                .build()
        );
    }
}
