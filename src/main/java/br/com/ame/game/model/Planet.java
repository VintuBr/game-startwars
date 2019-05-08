package br.com.ame.game.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "PLANET")
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PLANET_UID", unique = true, updatable = false)
    private Long planetUid;

    @NotBlank
    @Column(name = "NAME", unique = true)
    private String name;

    @NotBlank
    @Column(name = "WEATHER")
    private String weather;

    @NotBlank
    @Column(name = "TERRAIN")
    private String terrain;

    @NotBlank
    @Column(name = "NUM_FILMS")
    private Integer numFilms;

    public Planet(@NotBlank String name, @NotBlank String weather, @NotBlank String terrain, @NotBlank Integer numFilms) {
        this.name = name;
        this.weather = weather;
        this.terrain = terrain;
        this.numFilms = numFilms;
    }
}
