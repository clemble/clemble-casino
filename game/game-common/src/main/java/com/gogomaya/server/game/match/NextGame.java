package com.gogomaya.server.game.match;

import java.util.Collection;

import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.specification.GameSpecificationAware;

public class NextGame implements GameSpecificationAware {

    private GameSpecification specification;

    private Collection<Long> players;

    @Override
    public GameSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(GameSpecification specification) {
        this.specification = specification;
    }

    public Collection<Long> getPlayers() {
        return players;
    }

    public void setPlayers(Collection<Long> players) {
        this.players = players;
    }

}
