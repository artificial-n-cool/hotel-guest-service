package com.artificialncool.guestapp.integration;

import com.artificialncool.guestapp.dto.converter.OcenaKorisnikaConverter;
import com.artificialncool.guestapp.dto.converter.OcenaSmestajConverter;
import com.artificialncool.guestapp.dto.converter.RezervacijaConverter;
import com.artificialncool.guestapp.dto.converter.SmestajConverter;
import com.artificialncool.guestapp.dto.model.OcenaKorisnikaDTO;
import com.artificialncool.guestapp.dto.model.RezervacijaDTO;
import com.artificialncool.guestapp.model.Korisnik;
import com.artificialncool.guestapp.model.Smestaj;
import com.artificialncool.guestapp.repository.KorisnikRepository;
import com.artificialncool.guestapp.repository.SmestajRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;

public class TestGuestAppIntegration extends AbstractIntegrationTest{

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SmestajRepository smestajRepository;

    @Autowired
    KorisnikRepository korisnikRepository;

    @Autowired
    OcenaKorisnikaConverter ocenaKorisnikaConverter;

    @Autowired
    OcenaSmestajConverter ocenaSmestajConverter;

    @Autowired
    RezervacijaConverter rezervacijaConverter;

    @Autowired
    SmestajConverter smestajConverter;

    @BeforeEach
    void setup(){
        smestajRepository.deleteAll();
        korisnikRepository.deleteAll();
    }

    @Test
    public void shouldUpdateOcenaHosta() throws Exception{

        // dodaj korisnika u bazu

        Korisnik korisnik = Korisnik.builder()
                .ime("Kobe")
                .prezime("Bryant")
                .username("Mamba24")
                .password("12345")
                .email("blackmamba@gmail.com")
                .prebivalste("Los Angeles")
                .prosecnaOcena(0.0)
                .build();

        Korisnik korisnik1 = korisnikRepository.save(korisnik);

        //kreiraj DTO, kao sa fronta da je dosao

        OcenaKorisnikaDTO ocenaKorisnikaDTO = OcenaKorisnikaDTO.builder()
                .ocena(5.0)
                .datum(LocalDateTime.now())
                .hostId(korisnik1.getId())
                .ocenjivacId("2")
                .build();

        // onda pravis API call i testiraj posledice

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/api/guest/smestaj/oceniHosta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(new ObjectMapper().writeValueAsString(ocenaKorisnikaDTO));

        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().string("5.0"));
    }

    @Test
    public void shouldSaveOneRezervacija() throws Exception{

        // dodaj smestaj u bazu

        Smestaj smestaj = Smestaj.builder()
                .naziv("Apartmani Brdo")
                .lokacija("Banovo Brdo")
                .pogodnosti("Wifi, Klima, Internet, Kablovska")
                .opis("Halo najace")
                .vlasnikID("4025ti4j042tu")
                .minGostiju(1)
                .maxGostiju(3)
                .build();

        smestajRepository.save(smestaj);

        //kreriraj DTO, kao sa fronta da je dosao
        RezervacijaDTO rezervacijaDTO = RezervacijaDTO.builder()
                .brojOsoba(4)
                .datumDo(LocalDate.now())
                .datumOd(LocalDate.now())
                .smestajId(smestaj.getId())
                .statusRezervacije("U_OBRADI")
                .ocenjivacId("1")
                .build();

        //mockmvc poziv
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/api/guest/smestaj/rezervisiSmestaj")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(new ObjectMapper().writeValueAsString(rezervacijaDTO));

        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andDo(print());

        Smestaj smestaj1 = smestajRepository.findById(smestaj.getId()).orElseThrow();
        assertEquals(smestaj1.getRezervacije().size(), 1);
        assertNotNull(smestaj1.getRezervacije().get(0));
    }
}
