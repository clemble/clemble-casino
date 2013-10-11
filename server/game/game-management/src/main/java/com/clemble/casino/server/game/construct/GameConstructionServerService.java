package com.clemble.casino.server.game.construct;

import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameRequest;
import com.clemble.casino.game.event.schedule.InvitationResponseEvent;

public interface GameConstructionServerService {

    public GameConstruction construct(GameRequest request);

    public GameConstruction invitationResponsed(InvitationResponseEvent response);

}
