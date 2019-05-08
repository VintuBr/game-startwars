package br.com.ame.game.gateway.domain;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Wither;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SWList<T> implements Serializable {
    public int count;
    public String next;
    public String previous;
    public List<T> results;

    public SWList(int count, List<T> results) {
        this.count = count;
        this.results = results;
    }

    public boolean hasMore() {
        return (next != null && !next.isEmpty());
    }
}
