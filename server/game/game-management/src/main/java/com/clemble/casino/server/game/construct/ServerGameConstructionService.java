package com.clemble.casino.server.game.construct;

import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.PlayerGameConstructionRequest;
import com.clemble.casino.game.event.schedule.InvitationResponseEvent;
import com.clemble.casino.server.ServerService;

public interface ServerGameConstructionService extends ServerService {

    public GameConstruction construct(PlayerGameConstructionRequest request);

    public GameConstruction invitationResponsed(InvitationResponseEvent response);

}
