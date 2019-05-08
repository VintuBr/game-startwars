package br.com.ame.game.gateway;

import br.com.ame.game.gateway.domain.SWModelList;
import br.com.ame.game.gateway.domain.SWPlanet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SWApiGatewayImpl implements SWApiGateway {

    @Value("${swapi.planet.uri:https://swapi.co/api/planets/}")
    private String swApiPlanetUri;

    @Value("${swapi.planet.search:?search=}")
    private String swApiPlanetSearch;

    private WebClient webClient;

    @Autowired
    public SWApiGatewayImpl(final WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(swApiPlanetUri).build();
    }

    public Mono<SWModelList<SWPlanet>> getPlanetByName(String name) {
        log.info("Using Planets: [{}]", swApiPlanetUri+swApiPlanetSearch+name);

        return webClient.get()
                .uri(swApiPlanetUri+swApiPlanetSearch+name)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SWModelList<SWPlanet>>(){});
    }
}
