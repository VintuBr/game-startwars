package br.com.ame.game.service;

import br.com.ame.game.domain.PlanetContent;
import br.com.ame.game.gateway.domain.SWModelList;
import br.com.ame.game.gateway.domain.SWPlanet;
import br.com.ame.game.model.Planet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlanetService {
    Mono<Planet> save(Mono<PlanetContent> planet);

    Flux<PlanetContent> findAll();

    Flux<PlanetContent> getAllSWApi();

    Mono<PlanetContent> findByName(String name);

    Mono<PlanetContent> findById(Long id);

    Mono<Void> delete(Long id);

    Mono<SWModelList<SWPlanet>> getSWApiByName(String name);
}
