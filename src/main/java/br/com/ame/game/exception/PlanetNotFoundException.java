package br.com.ame.game.exception;

import static java.lang.String.format;

public class PlanetNotFoundException extends BusinessException {

    public PlanetNotFoundException(String externaCode, Long planetUid) {
        super(externaCode, planetUid, format("Planet is not found with id: '%s'", planetUid));
    }

    public PlanetNotFoundException(String externaCode, String planetName) {
        super(externaCode, planetName, format("Planet is not found with name: '%s'", planetName));
    }

    public PlanetNotFoundException(String externaCode) {
        super(externaCode, null, "No Planets found");
    }
}
