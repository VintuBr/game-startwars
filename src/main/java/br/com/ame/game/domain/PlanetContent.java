package br.com.ame.game.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@AllArgsConstructor
@ToString
@Builder
@Wither
@Getter
@Setter
public class PlanetContent {

    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String weather;

    @NonNull
    private String terrain;

    private Integer numFilms;
}
