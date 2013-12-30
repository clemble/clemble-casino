package com.clemble.casino.server.game.aspect.presence;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;

public class GameEndPresenceAspectFactory implements GameAspectFactory<GameEndedEvent<?>> {

    final private GameEndPresenceAspect endPresenceAspect;

    public GameEndPresenceAspectFactory(PlayerPresenceServerService presenceService) {
        this.endPresenceAspect = new GameEndPresenceAspect(presenceService);
    }

    @Override
    public GameAspect<GameEndedEvent<?>> construct(GameInitiation initiation, GameContext construction) {
        return endPresenceAspect;
    }

}
