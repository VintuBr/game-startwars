package br.com.ame.game.gateway;

import br.com.ame.game.gateway.domain.SWModelList;
import br.com.ame.game.gateway.domain.SWPlanet;
import reactor.core.publisher.Mono;

public interface SWApiGateway {
    Mono<SWModelList<SWPlanet>> getPlanetByName(String name);
}
