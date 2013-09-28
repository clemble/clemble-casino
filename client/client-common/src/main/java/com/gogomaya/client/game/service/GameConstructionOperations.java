package com.gogomaya.client.game.service;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.event.listener.EventListener;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameRequest;
import com.gogomaya.game.event.schedule.InvitationResponseEvent;

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
