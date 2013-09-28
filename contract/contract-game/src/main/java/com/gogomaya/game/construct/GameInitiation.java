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
    final private LinkedHashSet<String> participants;

    public GameInitiation(GameConstruction construction) {
        this.specification = construction.getRequest().getSpecification();
        this.session = construction.getSession();

        final Collection<String> accpectedParticipants = construction.fetchAcceptedParticipants();
        this.participants = accpectedParticipants instanceof LinkedHashSet ? (LinkedHashSet<String>) accpectedParticipants : new LinkedHashSet<String>(
                accpectedParticipants);
    }

    public GameInitiation(GameSessionKey session, Collection<String> participants, GameSpecification specification) {
        this.session = session;
        this.specification = checkNotNull(specification);
        this.participants = participants instanceof LinkedHashSet ? (LinkedHashSet<String>) participants : new LinkedHashSet<String>(participants);
    }

    @Override
    public GameSpecification getSpecification() {
        return specification;
    }

    @Override
    public Collection<String> getParticipants() {
        return participants;
    }

    @Override
    public GameSessionKey getSession() {
        return session;
    }
}
