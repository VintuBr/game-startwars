package br.com.ame.game.service;

import br.com.ame.game.domain.PlanetContent;
import br.com.ame.game.gateway.domain.SWList;
import br.com.ame.game.gateway.domain.SWPlanet;
import br.com.ame.game.model.Planet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlanetService {
    Mono<Planet> save(Mono<PlanetContent> planetContent);

    Mono<PlanetContent> saveBlock(PlanetContent planetContent);

    Flux<PlanetContent> findAll();

    Mono<SWList<SWPlanet>> getSWApiPage(Integer page);

    Mono<PlanetContent> findByName(String name);

    Mono<PlanetContent> findById(Long id);

    Mono<Void> delete(Mono<Long> id);

    Mono<SWList<SWPlanet>> getSWApiByName(String name);
}
