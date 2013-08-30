package com.gogomaya.game.service;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameRequest;
import com.gogomaya.game.event.schedule.InvitationResponseEvent;

public interface GameConstructionService {

    public GameConstruction construct(final long playerId, final GameRequest gameRequest);

    public GameConstruction getConstruct(final long playerId, final long session);

    public ClientEvent getResponce(final long requester, final long session, final long player);

    public GameConstruction invitationResponsed(final long playerId, long sessionId, final InvitationResponseEvent gameRequest);

}
