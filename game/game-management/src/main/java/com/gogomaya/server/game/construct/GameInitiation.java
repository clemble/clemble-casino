package com.gogomaya.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.gogomaya.server.game.ConstructionAware;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.specification.GameSpecificationAware;

public class GameInitiation implements GameOpponentsAware, GameSpecificationAware, ConstructionAware {

    /**
     * Generated 10/07/13
     */
    private static final long serialVersionUID = -8584404446775359390L;

    final private long construction;

    final private GameSpecification specification;

    final private LinkedHashSet<Long> participants;

    public GameInitiation(long construction, Collection<Long> participants, GameSpecification specification) {
        this.construction = construction;
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
    public long getConstruction() {
        return construction;
    }
}
