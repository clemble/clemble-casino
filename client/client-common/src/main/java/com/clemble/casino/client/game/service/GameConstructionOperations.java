package com.clemble.casino.client.game.service;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.event.listener.EventListener;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameRequest;
import com.clemble.casino.game.event.schedule.InvitationResponseEvent;

public interface GameConstructionOperations<T extends GameState> {

    public GameConstruction getConstruct(final String session);

    public ClientEvent getResponce(final String session, final String player);

    public GameConstruction construct(final GameRequest gameRequest);

    public GameConstruction accept(final String sessionId);

    public GameConstruction decline(final String sessionId);

    public GameConstruction response(final String sessionId, final InvitationResponseEvent gameRequest);

    public void subscribe(String sessionId, EventListener constructionListener);

    public GameActionOperations<T> getActionOperations(GameSessionKey gameSessionKey);

}
