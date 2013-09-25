package com.gogomaya.game.construct;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.SessionAware;
import com.gogomaya.game.specification.GameSpecification;
import com.gogomaya.game.specification.GameSpecificationAware;

public class GameInitiation implements GameOpponentsAware, GameSpecificationAware, SessionAware {

    /**
     * Generated 10/07/13
     */
    private static final long serialVersionUID = -8584404446775359390L;

    final private GameSessionKey session;
    final private GameSpecification specification;
    final private LinkedHashSet<Long> participants;

    public GameInitiation(GameConstruction construction) {
        this.specification = construction.getRequest().getSpecification();
        this.session = construction.getSession();

        final Collection<Long> accpectedParticipants = construction.fetchAcceptedParticipants();
        this.participants = accpectedParticipants instanceof LinkedHashSet ? (LinkedHashSet<Long>) accpectedParticipants : new LinkedHashSet<Long>(
                accpectedParticipants);
    }

    public GameInitiation(GameSessionKey session, Collection<Long> participants, GameSpecification specification) {
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
    public GameSessionKey getSession() {
        return session;
    }
}
