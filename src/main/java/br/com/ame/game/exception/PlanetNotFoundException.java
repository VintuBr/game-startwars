package br.com.ame.game.exception;

import static java.lang.String.format;

public class PlanetNotFoundException extends BusinessException {

    public PlanetNotFoundException(String externaCode, Long planedUid) {
        super(externaCode, planedUid, format("Planet is not found with id : '%s'", planedUid));
    }
}
