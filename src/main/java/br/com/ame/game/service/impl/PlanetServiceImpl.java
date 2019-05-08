package br.com.ame.game.service.impl;

import static java.util.Optional.ofNullable;

import br.com.ame.game.domain.PlanetContent;
import br.com.ame.game.exception.PlanetAlreadyExistsException;
import br.com.ame.game.exception.PlanetNotFoundException;
import br.com.ame.game.gateway.SWApiGateway;
import br.com.ame.game.gateway.domain.SWList;
import br.com.ame.game.gateway.domain.SWPlanet;
import br.com.ame.game.model.Planet;
import br.com.ame.game.repository.PlanetRepository;
import br.com.ame.game.service.PlanetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
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

    @Override
    public Mono<PlanetContent> saveBlock(PlanetContent planetContent) {
         if(ofNullable(planetRepository.findByName(planetContent.getName())).isPresent()) {
             throw new PlanetAlreadyExistsException("PLANET_ALREADY_EXISTS", planetContent.getName());
         }

        SWList<SWPlanet> swList = swApiGateway.getPlanetByNameBlock(planetContent.getName());

        Planet planet = mapToBasePlanet(planetContent);
        planet.setNumFilms(getNumFilms(swList));

        log.info("Saving planet: [{}]", planet);

        planetRepository.save(planet);

        planetContent.setId(planet.getPlanetUid());
        planetContent.setNumFilms(planet.getNumFilms());

        return Mono.just(planetContent);
    }

    private Planet mapToPlanet(PlanetContent planetContent) {
        Planet planet = mapToBasePlanet(planetContent);

        swApiGateway.getPlanetByName(planetContent.getName())
                .subscribeOn(Schedulers.elastic())
                .map(this::getNumFilms).doOnNext(i -> planet.setNumFilms(i));

        return planet;
    }

    private Planet mapToBasePlanet(PlanetContent planetContent) {
        return new Planet(planetContent.getName(), planetContent.getWeather(),
                planetContent.getTerrain(), 0);
    }

    private Integer getNumFilms(SWList<SWPlanet> swList) {
        if(swList.getCount() == 1) {
            return swList.getResults().get(0).getFilmsUrls().size();
        } else if(swList.getCount() > 1) {
            throw new PlanetNotFoundException("MULTIPLE_PLANETS_FOUND");
        }

        return 0;
    }

    @Override
    public Flux<PlanetContent> findAll() {
        return Flux.defer(() -> Flux.fromIterable(planetRepository.findAll()))
                .subscribeOn(Schedulers.elastic())
                .map(planet -> new PlanetContent(planet.getPlanetUid(), planet.getName(), planet.getWeather(), planet.getTerrain(), planet.getNumFilms()));
    }

    @Override
    public Mono<SWList<SWPlanet>> getSWApiPage(Integer page) {
        return swApiGateway.getAllPlanetsPage(page);
    }

    @Override
    public Mono<PlanetContent> findByName(String name) {
        return Mono.defer(() -> Mono.justOrEmpty(planetRepository.findByName(name)))
                .subscribeOn(Schedulers.elastic())
                .map(planet -> new PlanetContent(planet.getPlanetUid(), planet.getName(), planet.getWeather(), planet.getTerrain(), planet.getNumFilms()));
    }

    @Override
    public Mono<PlanetContent> findById(Long id) {
        return Mono.defer(() -> Mono.justOrEmpty(planetRepository.findById(id)))
                .subscribeOn(Schedulers.elastic())
                .map(planet -> new PlanetContent(planet.getPlanetUid(), planet.getName(),
                        planet.getWeather(), planet.getTerrain(), planet.getNumFilms()));
    }

    @Override
    public Mono<SWList<SWPlanet>> getSWApiByName(String name) {
        return swApiGateway.getPlanetByName(name);
    }

    @Override
    public Mono<Void> delete(Mono<Long> id) {
        planetRepository.deleteById(id.block());
        return id.then();
    }
}
