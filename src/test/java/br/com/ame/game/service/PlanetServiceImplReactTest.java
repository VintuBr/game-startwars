package br.com.ame.game.service;

import static br.com.ame.game.mother.PlanetContentMother.PLANET_CONTENT_ALDERAAN;
import static br.com.ame.game.mother.PlanetContentMother.PLANET_CONTENT_TATOOINE;
import static br.com.ame.game.mother.SWPlanetMother.SWLIST_ONE_PLANET;
import static br.com.ame.game.mother.SWPlanetMother.SWLIST_TWO_PLANETS;
import static org.junit.Assert.assertEquals;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import br.com.ame.game.controller.PlanetController;
import br.com.ame.game.domain.ErrorResponse;
import br.com.ame.game.domain.PlanetContent;
import br.com.ame.game.gateway.domain.SWList;
import br.com.ame.game.gateway.domain.SWPlanet;
import br.com.ame.game.mother.SWPlanetMother;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest(PlanetController.class)
@FixMethodOrder(NAME_ASCENDING)
public class PlanetServiceImplReactTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PlanetService planetService;

    @Test
    public void testFindAllPlanets() {
        when(planetService.findAll())
                .thenReturn(Flux.just(PLANET_CONTENT_TATOOINE, PLANET_CONTENT_ALDERAAN));

        webTestClient.get()
                .uri("/planets/")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PlanetContent.class)
                .hasSize(2)
                .contains(PLANET_CONTENT_TATOOINE, PLANET_CONTENT_ALDERAAN);
    }

    @Test
    public void testFindAllPlanetsNoPlanetFound() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setHttpCode(404);
        errorResponse.setExternalErrorCode("NO_PLANET_FOUND");
        errorResponse.setErrorMessage("No Planets found");

        when(planetService.findAll()).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/planets/")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .isEqualTo(errorResponse);
    }

    @Test
    public void testFindPlanetByName() {
        when(planetService.findByName(eq(PLANET_CONTENT_TATOOINE.getName())))
                .thenReturn(Mono.just(PLANET_CONTENT_TATOOINE));

        webTestClient.get()
                .uri("/planets/?name=" + PLANET_CONTENT_TATOOINE.getName())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlanetContent.class)
                .isEqualTo(PLANET_CONTENT_TATOOINE);
    }

    @Test
    public void testFindPlanetByNameNoPlanetFound() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setHttpCode(404);
        errorResponse.setExternalErrorCode("PLANET_NAME_NOT_FOUND");
        errorResponse.setErrorMessage("Planet is not found with name: 'Tatooine'");
        errorResponse.setErrorCode(PLANET_CONTENT_TATOOINE.getName());

        when(planetService.findByName(eq(PLANET_CONTENT_TATOOINE.getName())))
                .thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/planets/?name="+PLANET_CONTENT_TATOOINE.getName())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .isEqualTo(errorResponse);
    }

    @Test
    public void testFindPlanetById() {
        Long testId = 1L;
        when(planetService.findById(eq(testId)))
                .thenReturn(Mono.just(PLANET_CONTENT_TATOOINE));

        webTestClient.get()
                .uri("/planets/" + testId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlanetContent.class)
                .isEqualTo(PLANET_CONTENT_TATOOINE);
    }

    @Test
    public void testFindPlanetByIdNoPlanetFound() {
        Integer testId = 1;

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setHttpCode(404);
        errorResponse.setExternalErrorCode("PLANET_ID_NOT_FOUND");
        errorResponse.setErrorMessage("Planet is not found with id: '1'");
        errorResponse.setErrorCode(testId);

        when(planetService.findById(eq(testId.longValue()))).thenReturn(Mono.empty());

        EntityExchangeResult<ErrorResponse> e = webTestClient.get()
                .uri("/planets/" + testId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class).returnResult();

        assertEquals(errorResponse.getExternalErrorCode(), e.getResponseBody().getExternalErrorCode());
        assertEquals(errorResponse.getErrorCode(), e.getResponseBody().getErrorCode());
        assertEquals(errorResponse.getErrorMessage(), e.getResponseBody().getErrorMessage());
        assertEquals(errorResponse.getHttpCode(), e.getResponseBody().getHttpCode());
    }

    @Test
    public void testGetSWApiByName() {
        when(planetService.getSWApiByName(eq(PLANET_CONTENT_TATOOINE.getName())))
                .thenReturn(Mono.just(SWLIST_ONE_PLANET));

        webTestClient.get()
                .uri("/planets/swapis/search?name=" + PLANET_CONTENT_TATOOINE.getName())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<SWList<SWPlanet>>(){})
                .isEqualTo(SWLIST_ONE_PLANET);
    }

    @Test
    public void testGetAllSWApi() {
        when(planetService.getSWApiPage(eq(1)))
                .thenReturn(Mono.just(SWLIST_ONE_PLANET));
        when(planetService.getSWApiPage(eq(2)))
                .thenReturn(Mono.just(SWLIST_TWO_PLANETS));

        webTestClient.get()
                .uri("/planets/swapis?page=" + 1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<SWList<SWPlanet>>(){})
                .isEqualTo(SWLIST_ONE_PLANET);

        webTestClient.get()
                .uri("/planets/swapis?page=" + 2)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<SWList<SWPlanet>>(){})
                .isEqualTo(SWLIST_TWO_PLANETS);
    }
}
