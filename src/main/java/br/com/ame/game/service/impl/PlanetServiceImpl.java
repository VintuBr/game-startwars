package br.com.ame.game.service.impl;

import br.com.ame.game.domain.PlanetContent;
import br.com.ame.game.gateway.SWApiGateway;
import br.com.ame.game.gateway.domain.SWModelList;
import br.com.ame.game.gateway.domain.SWPlanet;
import br.com.ame.game.model.Planet;
import br.com.ame.game.repository.PlanetRepository;
import br.com.ame.game.service.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class PlanetServiceImpl implements PlanetService {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private SWApiGateway swApiGateway;

    @Override
    public Mono<Planet> save(Mono<PlanetContent> planetContent) {
        return planetContent
                .map(this::mapToPlanet)
                .publishOn(Schedulers.parallel())
                .doOnNext(planetRepository::save);
    }

    private Planet mapToPlanet(PlanetContent planetContent) {
        Mono<SWModelList<SWPlanet>> swPlanet = swApiGateway.getPlanetByName(planetContent.getName());

        return new Planet(planetContent.getName(), planetContent.getWeather(),
                planetContent.getTerrain(), swPlanet.block().results.get(0).filmsUrls.size());
    }

    @Override
    public Flux<PlanetContent> findAll() {
        return null;
    }

    @Override
    public Flux<PlanetContent> getAllSWApi() {
        return null;
    }

    @Override
    public Mono<PlanetContent> findByName(String name) {
        return null;
    }

    @Override
    public Mono<PlanetContent> findById(Long id) {
        return Mono.defer(() -> Mono.justOrEmpty(planetRepository.findById(id)))
                .subscribeOn(Schedulers.elastic())
                .map(planet -> new PlanetContent(planet.getPlanetUid(), planet.getName(),
                        planet.getWeather(), planet.getTerrain(), planet.getNumFilms()));
    }

    @Override
    public Mono<SWModelList<SWPlanet>> getSWApiByName(String name) {
        return swApiGateway.getPlanetByName(name);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return null;
    }
}
