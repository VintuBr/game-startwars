package br.com.ame.game.gateway;

import br.com.ame.game.gateway.domain.SWList;
import br.com.ame.game.gateway.domain.SWPlanet;
import reactor.core.publisher.Mono;

public interface SWApiGateway {
    Mono<SWList<SWPlanet>> getPlanetByName(String name);
    SWList<SWPlanet> getPlanetByNameBlock(String name);
    Mono<SWList<SWPlanet>> getAllPlanetsPage(Integer page);
}
