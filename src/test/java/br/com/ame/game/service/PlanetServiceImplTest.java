package br.com.ame.game.service;

import static br.com.ame.game.mother.PlanetMother.PLANET_TATOOINE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.ame.game.domain.PlanetContent;
import br.com.ame.game.exception.PlanetAlreadyExistsException;
import br.com.ame.game.exception.PlanetNotFoundException;
import br.com.ame.game.gateway.SWApiGateway;
import br.com.ame.game.model.Planet;
import br.com.ame.game.mother.PlanetContentMother;
import br.com.ame.game.mother.SWPlanetMother;
import br.com.ame.game.repository.PlanetRepository;
import br.com.ame.game.service.impl.PlanetServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

@RunWith(MockitoJUnitRunner.class)
public class PlanetServiceImplTest {

    @InjectMocks
    private PlanetServiceImpl fixture;

    @Mock
    private PlanetRepository planetRepository;

    @Mock
    private SWApiGateway swApiGateway;

    @Test
    public void testSaveBlock() {
        when(planetRepository.findByName(eq(PLANET_TATOOINE.getName()))).thenReturn(null);
        when(swApiGateway.getPlanetByNameBlock(eq(PLANET_TATOOINE.getName()))).thenReturn(SWPlanetMother.SWLIST_ONE_PLANET);
        when(planetRepository.save(any(Planet.class))).thenReturn(PLANET_TATOOINE);

        Mono<PlanetContent> planetContent = fixture.saveBlock(PlanetContentMother.PLANET_CONTENT_TATOOINE);

        PlanetContent planetContentResponse = planetContent.block();
        assertEquals(PLANET_TATOOINE.getPlanetUid(), planetContentResponse.getId());
        assertEquals(PLANET_TATOOINE.getName(), planetContentResponse.getName());
        assertEquals(PLANET_TATOOINE.getTerrain(), planetContentResponse.getTerrain());
        assertEquals(PLANET_TATOOINE.getWeather(), planetContentResponse.getWeather());
        assertEquals(PLANET_TATOOINE.getNumFilms(), planetContentResponse.getNumFilms());

        verify(planetRepository).findByName(eq(PLANET_TATOOINE.getName()));
        verify(swApiGateway).getPlanetByNameBlock(eq(PLANET_TATOOINE.getName()));

        ArgumentCaptor<Planet> planetArgumentCaptor = ArgumentCaptor.forClass(Planet.class);
        verify(planetRepository).save(planetArgumentCaptor.capture());
        assertNotNull(planetArgumentCaptor.getValue());
        Planet planet = planetArgumentCaptor.getValue();
        assertEquals(PLANET_TATOOINE, planet);
    }

    @Test
    public void testSaveBlockAlreadyExists() {
        when(planetRepository.findByName(eq(PLANET_TATOOINE.getName()))).thenReturn(PLANET_TATOOINE);

        try {
            fixture.saveBlock(PlanetContentMother.PLANET_CONTENT_TATOOINE);
            fail();
        } catch (PlanetAlreadyExistsException e) {
            assertEquals("PLANET_ALREADY_EXISTS", e.getExternalErrorCode());
            assertEquals(PLANET_TATOOINE.getName(), e.getErrorCode());
        }

        verify(planetRepository).findByName(eq(PLANET_TATOOINE.getName()));
        verify(swApiGateway, never()).getPlanetByNameBlock(eq(PLANET_TATOOINE.getName()));
        verify(planetRepository, never()).save(any(Planet.class));
    }

    @Test
    public void testSaveBlockMoreThanOnePlanet() {
        when(planetRepository.findByName(eq(PLANET_TATOOINE.getName()))).thenReturn(null);
        when(swApiGateway.getPlanetByNameBlock(eq(PLANET_TATOOINE.getName()))).thenReturn(SWPlanetMother.SWLIST_TWO_PLANETS);

        try {
            fixture.saveBlock(PlanetContentMother.PLANET_CONTENT_TATOOINE);
            fail();
        } catch (PlanetNotFoundException e) {
            assertEquals("MULTIPLE_PLANETS_FOUND", e.getExternalErrorCode());
        }

        verify(planetRepository).findByName(eq(PLANET_TATOOINE.getName()));
        verify(swApiGateway).getPlanetByNameBlock(eq(PLANET_TATOOINE.getName()));
        verify(planetRepository, never()).save(any(Planet.class));
    }

    @Test
    public void testDelete() {
        Planet planet = PLANET_TATOOINE;
        planet.setPlanetUid(1L);

        fixture.delete(Mono.just(planet.getPlanetUid()));
        verify(planetRepository).deleteById(eq(planet.getPlanetUid()));
    }
}
