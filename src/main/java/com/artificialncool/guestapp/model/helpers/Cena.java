package com.artificialncool.guestapp.model.helpers;


import com.artificialncool.guestapp.model.enums.TipCene;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("cene")
public class Cena {
    @Id
    private Long ID;
    private Double cena;
    private TipCene tipCene;
}
