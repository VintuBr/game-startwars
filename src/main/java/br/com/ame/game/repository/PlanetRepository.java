package br.com.ame.game.repository;

import br.com.ame.game.model.Planet;
import org.springframework.data.repository.CrudRepository;

public interface PlanetRepository extends CrudRepository<Planet, Long> {
    Planet findByName(String name);
}
