package com.clemble.casino.game.construct;

import java.util.List;

import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.specification.GameConfigurationAware;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.player.PlayerAwareUtils;

public class ServerGameInitiation implements GameSessionAware, GameConfigurationAware {

    /**
     * Generated 23/01/14
     */
    private static final long serialVersionUID = -3413033971589908905L;

    final private MatchGameContext context;
    final private GameSessionKey sessionKey;
    final private MatchGameConfiguration configuration;
    final private List<String> participants;

    public ServerGameInitiation(GameSessionKey sessionKey, MatchGameContext context, MatchGameConfiguration specification) {
        this.context = context;
        this.sessionKey = sessionKey;
        this.configuration = specification;
        this.participants = PlayerAwareUtils.toPlayerList(context.getPlayerContexts());
    }

    @Override
    public GameSessionKey getSession() {
        return sessionKey;
    }

    @Override
    public MatchGameConfiguration getConfiguration() {
        return configuration;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public MatchGameContext getContext() {
        return context;
    }

}
