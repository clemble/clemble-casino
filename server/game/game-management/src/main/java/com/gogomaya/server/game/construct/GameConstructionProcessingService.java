package com.gogomaya.server.game.construct;

import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameRequest;
import com.gogomaya.game.event.schedule.InvitationResponseEvent;

public interface GameConstructionProcessingService {

    public GameConstruction construct(GameRequest request);

    public GameConstruction invitationResponsed(InvitationResponseEvent response);

}
