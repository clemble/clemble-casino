package com.clemble.casino.server.game.aspect.record;

import com.clemble.casino.event.GameEvent;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.RoundGameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.game.aspect.RoundGameAspectFactory;
import com.clemble.casino.server.repository.game.MadeMoveRepository;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 10/03/14.
 */
public class RoundRecordGameAspectFactory implements RoundGameAspectFactory<GameEvent> {

    final private MadeMoveRepository moveRepository;

    public RoundRecordGameAspectFactory(MadeMoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    @Override
    public GameAspect<GameEvent> construct(RoundGameConfiguration configuration, RoundGameContext context) {
        return new RoundRecordGameAspect(moveRepository);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
