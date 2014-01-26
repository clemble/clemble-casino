package com.clemble.casino.server.game.aspect.pot;

import org.springframework.core.Ordered;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;

public class PotRuleAspectFactory implements GameAspectFactory<GameEndedEvent<?>> {


    @Override
    public GameAspect<GameEndedEvent<?>> construct(GameInitiation initiation, GameContext construction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

}
