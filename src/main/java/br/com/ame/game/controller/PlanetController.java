package br.com.ame.game.controller;

import static reactor.core.publisher.Mono.error;

import br.com.ame.game.domain.PlanetContent;
import br.com.ame.game.exception.PlanetNotFoundException;
import br.com.ame.game.gateway.domain.SWModelList;
import br.com.ame.game.gateway.domain.SWPlanet;
import br.com.ame.game.model.Planet;
import br.com.ame.game.service.PlanetService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@CrossOrigin
public class PlanetController {

    @Autowired
    private PlanetService planetService;

    @PostMapping("/planets")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Planet> create(@Valid @RequestBody Mono<PlanetContent> planetContent) {
        return planetService.save(planetContent);
    }

    @GetMapping(path = "/planets", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<PlanetContent> findAll() {
        return planetService.findAll();
    }

    @GetMapping(path = "/planets/swapi", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<PlanetContent> getAllSWApi() {
        return planetService.getAllSWApi();
    }

    @GetMapping("/planets")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PlanetContent> findByName(@RequestParam(value = "name") String name) {
        return planetService.findByName(name);
    }

    @GetMapping("/planets/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PlanetContent> findById(@PathVariable(value = "id") Long id) {
        return planetService.findById(id)
                .switchIfEmpty(error(new PlanetNotFoundException("PLANET_ID_NOT_FOUND", id)))
                .doOnNext(pc -> log.info("Planet: [{}]", pc));
    }

    @GetMapping(path = "/planets/swapiName")
    @ResponseStatus(HttpStatus.OK)
    public Mono<SWModelList<SWPlanet>> getSWApiByName(@RequestParam(value = "name") String name) {
        return planetService.getSWApiByName(name)
                .doOnNext(pc -> log.info("Planet: [{}]", pc));
    }

    @DeleteMapping("/planets/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Void> delete(@PathVariable(value = "id") Long id) {
        return planetService.delete(id);
    }
}
