package com.clemble.casino.server.game.aspect.security;

import com.clemble.casino.event.PlayerAwareEvent;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;

public class GameSecurityAspectFactory implements GameAspectFactory<PlayerAwareEvent> {

    @Override
    public GameAspect<PlayerAwareEvent> construct(GameInitiation initiation, GameContext context) {
        return new GameSecurityAspect(PlayerAwareUtils.toPlayerList(initiation.getParticipants()));
    }

}
