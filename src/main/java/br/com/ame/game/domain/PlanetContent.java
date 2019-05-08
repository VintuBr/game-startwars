package br.com.ame.game.domain;

import br.com.ame.game.model.Planet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.Wither;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class PlanetContent {

    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String weather;

    @NonNull
    private String terrain;

    private Integer numFilms;

    public PlanetContent(Planet planet) {
        this.id = planet.getPlanetUid();
        this.name = planet.getName();
        this.weather = planet.getWeather();
        this.terrain = planet.getTerrain();
        this.numFilms = planet.getNumFilms();
    }
}
