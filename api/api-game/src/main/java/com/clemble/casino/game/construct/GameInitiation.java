package com.clemble.casino.game.construct;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.game.specification.GameSpecificationAware;

public class GameInitiation implements GameOpponentsAware, GameSpecificationAware, GameSessionAware {

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
        this.participants = accpectedParticipants instanceof LinkedHashSet ? (LinkedHashSet<String>) accpectedParticipants : new LinkedHashSet<String>(accpectedParticipants);
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
