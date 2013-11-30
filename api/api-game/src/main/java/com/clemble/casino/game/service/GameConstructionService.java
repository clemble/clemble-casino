package com.clemble.casino.game.service;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.PlayerGameConstructionRequest;
import com.clemble.casino.game.event.schedule.InvitationResponseEvent;

public interface GameConstructionService {

    public GameConstruction construct(final PlayerGameConstructionRequest gameRequest);

    public GameConstruction getConstruct(final Game game, final String session);

    public ClientEvent getResponce(final Game game, final String session, final String player);

    public GameConstruction reply(final Game game, String sessionId, final InvitationResponseEvent gameRequest);

}
