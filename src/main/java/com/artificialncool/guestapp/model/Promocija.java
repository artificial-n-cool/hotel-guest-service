package com.artificialncool.guestapp.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Promocija {
    @Id
    private String id;
    private LocalDate datumOd;
    private LocalDate datumDo;
    private Double procenat;
    private List<DayOfWeek> dani;
}
