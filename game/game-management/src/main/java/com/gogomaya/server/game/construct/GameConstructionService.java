package com.gogomaya.server.game.construct;

import com.gogomaya.server.game.event.schedule.InvitationResponceEvent;

public interface GameConstructionService {

    public GameConstruction construct(GameRequest request);

    public GameConstruction invitationResponsed(InvitationResponceEvent response);

}
