package com.gogomaya.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.specification.GameSpecificationAware;

public class GameInitiation implements GameOpponentsAware, GameSpecificationAware, SessionAware {

    /**
     * Generated 10/07/13
     */
    private static final long serialVersionUID = -8584404446775359390L;

    final private long session;

    final private GameSpecification specification;

    final private LinkedHashSet<Long> participants;

    public GameInitiation(long session, Collection<Long> participants, GameSpecification specification) {
        this.session = session;
        this.specification = checkNotNull(specification);
        this.participants = participants instanceof LinkedHashSet ? (LinkedHashSet<Long>) participants : new LinkedHashSet<Long>(participants);
    }

    @Override
    public GameSpecification getSpecification() {
        return specification;
    }

    @Override
    public Collection<Long> getParticipants() {
        return participants;
    }

    @Override
    public long getSession() {
        return session;
    }
}
