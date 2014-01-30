package com.clemble.casino.game.construct;

import java.util.List;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.player.PlayerAwareUtils;

public class ServerGameInitiation implements GameSessionAware {

    /**
     * Generated 23/01/14
     */
    private static final long serialVersionUID = -3413033971589908905L;

    final private GameContext context;
    final private GameSessionKey sessionKey;
    final private MatchGameConfiguration specification;
    final private List<String> participants;

    public ServerGameInitiation(GameSessionKey sessionKey, GameContext context, MatchGameConfiguration specification) {
        this.context = context;
        this.sessionKey = sessionKey;
        this.specification = specification;
        this.participants = PlayerAwareUtils.toPlayerList(context.getPlayerContexts());
    }

    @Override
    public GameSessionKey getSession() {
        return sessionKey;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public MatchGameConfiguration getSpecification() {
        return specification;
    }

    public GameContext getContext() {
        return context;
    }

}
