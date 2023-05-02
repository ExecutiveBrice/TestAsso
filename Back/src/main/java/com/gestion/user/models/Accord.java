package com.gestion.user.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(	name = "accords",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id"),
        })
public class Accord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nom;

    private Boolean etat = null;

    private LocalDate datePassage = null;

    public Accord(String nom) {
        this.nom = nom;

    }
}
