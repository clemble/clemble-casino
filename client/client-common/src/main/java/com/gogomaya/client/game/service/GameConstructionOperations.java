package com.gogomaya.client.game.service;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameRequest;
import com.gogomaya.game.event.schedule.InvitationResponseEvent;

public interface GameConstructionOperations {

    public GameConstruction construct(final GameRequest gameRequest);

    public GameConstruction getConstruct(final long session);

    public ClientEvent getResponce(final long session, final long player);

    public GameConstruction invitationResponsed(final long sessionId, final InvitationResponseEvent gameRequest);


}
