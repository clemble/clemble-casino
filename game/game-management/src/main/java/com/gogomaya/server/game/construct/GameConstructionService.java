package com.gogomaya.server.game.construct;

import com.gogomaya.server.game.event.schedule.InvitationResponseEvent;

public interface GameConstructionService {

    public GameConstruction construct(GameRequest request);

    public GameConstruction invitationResponsed(InvitationResponseEvent response);

}
