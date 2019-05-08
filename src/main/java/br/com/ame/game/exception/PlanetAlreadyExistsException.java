package br.com.ame.game.exception;

import static java.lang.String.format;

public class PlanetAlreadyExistsException extends BusinessException {
    public PlanetAlreadyExistsException(String externaCode, String planetName) {
        super(externaCode, planetName, format("Planet found with name: '%s'", planetName));
    }
}
