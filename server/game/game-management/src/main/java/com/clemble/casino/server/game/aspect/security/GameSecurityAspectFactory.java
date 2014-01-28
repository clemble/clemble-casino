package com.clemble.casino.server.game.aspect.security;

import org.springframework.core.Ordered;

import com.clemble.casino.event.PlayerAwareEvent;
import com.clemble.casino.game.construct.ServerGameInitiation;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;

public class GameSecurityAspectFactory implements GameAspectFactory<PlayerAwareEvent> {

    @Override
    public GameAspect<PlayerAwareEvent> construct(ServerGameInitiation initiation) {
        return new GameSecurityAspect(initiation.getParticipants());
    }

    @Override
    public int getOrder(){
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
