package com.gogomaya.server.game.construct;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.gogomaya.game.SessionAware;
import com.gogomaya.server.game.Game;
import com.gogomaya.server.game.GameAware;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.specification.GameSpecificationAware;

public class GameInitiation implements GameOpponentsAware, GameSpecificationAware, SessionAware, GameAware {

    /**
     * Generated 10/07/13
     */
    private static final long serialVersionUID = -8584404446775359390L;

    final private Game game;

    final private long session;

    final private GameSpecification specification;

    final private LinkedHashSet<Long> participants;

    public GameInitiation(GameConstruction construction) {
        this.specification = construction.getRequest().getSpecification();
        this.game = specification.getName().getGame();
        this.session = construction.getSession();

        final Collection<Long> accpectedParticipants = construction.fetchAcceptedParticipants();
        this.participants = accpectedParticipants instanceof LinkedHashSet ? (LinkedHashSet<Long>) accpectedParticipants : new LinkedHashSet<Long>(
                accpectedParticipants);
    }

    public GameInitiation(final Game game, long session, Collection<Long> participants, GameSpecification specification) {
        this.game = game;
        this.session = session;
        this.specification = checkNotNull(specification);
        this.participants = participants instanceof LinkedHashSet ? (LinkedHashSet<Long>) participants : new LinkedHashSet<Long>(participants);
    }

    @Override
    public Game getGame() {
        return game;
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
