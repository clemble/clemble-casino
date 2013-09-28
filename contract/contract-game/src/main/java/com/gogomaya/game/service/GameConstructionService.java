package com.gogomaya.game.service;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameRequest;
import com.gogomaya.game.event.schedule.InvitationResponseEvent;

public interface GameConstructionService {

    public GameConstruction construct(final String player, final GameRequest gameRequest);

    public GameConstruction getConstruct(final String player, final long session);

    public ClientEvent getResponce(final String requester, final long session, final String player);

    public GameConstruction reply(final String player, long sessionId, final InvitationResponseEvent gameRequest);

    public String getServer(final String player, long sessionId);

}
