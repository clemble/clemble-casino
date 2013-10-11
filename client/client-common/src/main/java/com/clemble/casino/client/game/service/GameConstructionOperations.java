package com.clemble.casino.client.game.service;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.event.listener.EventListener;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameRequest;
import com.clemble.casino.game.event.schedule.InvitationResponseEvent;

public interface GameConstructionOperations {

    public GameConstruction getConstruct(final long session);

    public ClientEvent getResponce(final long session, final String player);

    public String getGameActionServer(final long sessionId);

    public GameConstruction construct(final GameRequest gameRequest);

    public GameConstruction accept(final long sessionId);

    public GameConstruction decline(final long sessionId);

    public GameConstruction response(final long sessionId, final InvitationResponseEvent gameRequest);

    public void subscribe(long sessionId, EventListener constructionListener);

}
