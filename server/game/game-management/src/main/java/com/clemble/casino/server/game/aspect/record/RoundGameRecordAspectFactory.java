package com.clemble.casino.server.game.aspect.record;

import com.clemble.casino.event.GameEvent;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.game.repository.GameRecordRepository;
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
        return new GameRecordEventAspect(context.getSessionKey(), recordRepository);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
