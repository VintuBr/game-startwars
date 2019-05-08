package br.com.ame.game.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "PLANET")
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLANET_UID", unique = true, updatable = false)
    private Long planetUid;

    @Column(name = "NAME", unique = true)
    private String name;

    @Column(name = "WEATHER")
    private String weather;

    @Column(name = "TERRAIN")
    private String terrain;

    @Column(name = "NUM_FILMS")
    private Integer numFilms;

    public Planet(String name, String weather, String terrain, Integer numFilms) {
        this.name = name;
        this.weather = weather;
        this.terrain = terrain;
        this.numFilms = numFilms;
    }
}
