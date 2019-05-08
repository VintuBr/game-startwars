package br.com.ame.game.mother;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import br.com.ame.game.gateway.domain.SWList;
import br.com.ame.game.gateway.domain.SWPlanet;
import br.com.ame.game.model.Planet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SWPlanetMother {
    public static final SWList<SWPlanet> SWLIST_ONE_PLANET = getSwlistOnePlanet();
    public static final SWList<SWPlanet> SWLIST_TWO_PLANETS = getSwlistTwoPlanets();

    private static SWList<SWPlanet> getSwlistTwoPlanets() {
        List<SWPlanet> planets = asList(mapToSWPlanet(PlanetMother.PLANET_ALDERAAN), mapToSWPlanet(PlanetMother.PLANET_TATOOINE));

        return new SWList<>(planets.size(), planets);
    }

    private static SWList<SWPlanet> getSwlistOnePlanet() {
        List<SWPlanet> planets = asList(mapToSWPlanet(PlanetMother.PLANET_TATOOINE));

        return new SWList<>(planets.size(), planets);
    }

    private static SWPlanet mapToSWPlanet(Planet planet) {
         List<String> filmUrls = range(1, planet.getNumFilms()+1)
                 .boxed()
                 .map(fn -> format("Film URL [%s]", fn))
                 .collect(toList());

        return SWPlanet.builder()
                .climate(planet.getWeather())
                .terrain(planet.getTerrain())
                .name(planet.getName())
                .filmsUrls(filmUrls)
                .created(LocalDate.of(2019, 10, 10).format(DateTimeFormatter.ISO_DATE))
                .build();
    }
}
