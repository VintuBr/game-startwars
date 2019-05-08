package br.com.ame.game.gateway;

import static java.util.Arrays.asList;

import br.com.ame.game.gateway.domain.SWList;
import br.com.ame.game.gateway.domain.SWPlanet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SWApiGatewayImpl implements SWApiGateway {

    @Value("${swapi.planet.uri:https://swapi.co/api/planets/}")
    private String swApiPlanetUri;

    @Value("${swapi.planet.search:?search=}")
    private String swApiPlanetSearch;

    @Value("${swapi.planet.page:?page=}")
    private String swApiPlanetPage;

    private WebClient webClient;

    @Autowired
    public SWApiGatewayImpl(final WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(swApiPlanetUri).build();
    }

    public Mono<SWList<SWPlanet>> getPlanetByName(String name) {
        String searchUri = swApiPlanetUri+swApiPlanetSearch+name;
        log.info("Searching Planets: [{}]", searchUri);
        return getFromPlanetApi(searchUri);
    }

    public SWList<SWPlanet> getPlanetByNameBlock(String name) {
        String searchUri = swApiPlanetUri+swApiPlanetSearch+name;
        log.info("Searching Planets: [{}]", searchUri);
        return getFromPlanetApiBlock(searchUri);
    }

    public Mono<SWList<SWPlanet>> getAllPlanetsPage(Integer page) {
        String pageUri = swApiPlanetUri+swApiPlanetPage+page;
        log.info("Searching Planets page: [{}]", pageUri);
        return getFromPlanetApi(pageUri);
    }

    private Mono<SWList<SWPlanet>> getFromPlanetApi(String uri) {
        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SWList<SWPlanet>>(){})
                .onErrorReturn(new SWList<>());
    }

    private SWList<SWPlanet> getFromPlanetApiBlock(String uri) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(asList(MediaType.APPLICATION_JSON_UTF8));
        httpHeaders.add("User-Agent", "Chrome");

        final HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SWList<SWPlanet>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<SWList<SWPlanet>>(){});

        log.info("Response: [{}]", responseEntity);
        if(responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            return new SWList<>();
        }
    }
}
