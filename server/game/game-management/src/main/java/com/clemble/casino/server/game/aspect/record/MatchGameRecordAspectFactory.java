package com.clemble.casino.server.game.aspect.record;

import com.clemble.casino.event.GameEvent;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;
import com.clemble.casino.server.repository.game.MadeMoveRepository;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 12/03/14.
 */
public class MatchGameRecordAspectFactory implements MatchGameAspectFactory<GameEvent> {

    final private MadeMoveRepository moveRepository;

    public MatchGameRecordAspectFactory(MadeMoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    @Override
    public GameAspect<GameEvent> construct(MatchGameConfiguration initiation, MatchGameContext potContext) {
        return new MatchGameRecordAspect(potContext, moveRepository);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
