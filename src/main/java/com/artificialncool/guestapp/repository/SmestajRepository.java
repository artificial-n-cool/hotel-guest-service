package com.artificialncool.guestapp.repository;

import com.artificialncool.guestapp.model.Smestaj;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SmestajRepository extends MongoRepository<Smestaj, String> {
    //List<Smestaj> findByNazivIgnoreCase(String naziv);

    //List<Smestaj> findByLokacijaIgnoreCase(String lokacija);

    List<Smestaj> findByNazivContainsIgnoreCase(String naziv);

    List<Smestaj> findByLokacijaContainsIgnoreCase(String lokacija);

    List<Smestaj> findByProsecnaOcenaGreaterThanEqual(Double prosecnaOcena);

    List<Smestaj> findByVlasnikID(String vlasnikID);

    List<Smestaj> findByNazivContainsIgnoreCaseAndLokacijaContainsIgnoreCase(String naziv, String lokacija);


    long deleteByNaziv(String naziv);

    long deleteByLokacija(String lokacija);

    long deleteByVlasnikID(String vlasnikID);


    @Query("{" +
            "'lokacija': { $regex: ?0, $options: 'i' }," +
            "'maxGostiju': { $gte: ?1 }," +
            "'rezervacije': { $not: { $elemMatch: { " +
            "'datumOd': { $lt: ?3 }, " +
            "'datumDo': { $gt: ?2 } " +
            "} } }" +
            "}")
    Page<Smestaj> findAllByLocationAndGuestsAndNoReservationsBetween(String location, Integer numGuests, LocalDateTime from, LocalDateTime to, Pageable pageable);
}
