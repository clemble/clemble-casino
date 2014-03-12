package com.clemble.casino.server.game.aspect.record;

import com.clemble.casino.event.Event;
import com.clemble.casino.event.GameEvent;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameRecord;
import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.RoundGameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.game.aspect.RoundGameAspectFactory;
import com.clemble.casino.server.repository.game.GameRecordRepository;
import com.clemble.casino.server.repository.game.MadeMoveRepository;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 10/03/14.
 */
public class RoundGameRecordAspectFactory implements GameAspectFactory<GameEvent, GameContext<?>, GameConfiguration> {

    final private GameRecordRepository recordRepository;

    public RoundGameRecordAspectFactory(GameRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public GameAspect<GameEvent> construct(GameConfiguration configuration, GameContext<?> context) {
        return new GameRecordEventAspect(context.getSession(), recordRepository);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
