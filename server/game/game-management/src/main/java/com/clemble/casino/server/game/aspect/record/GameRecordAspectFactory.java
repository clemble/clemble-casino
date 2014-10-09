package com.clemble.casino.server.game.aspect.record;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.lifecycle.management.GameContext;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.game.lifecycle.management.GameState;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.game.aspect.GenericGameAspectFactory;
import com.clemble.casino.server.game.repository.GameRecordRepository;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 10/03/14.
 */
public class GameRecordAspectFactory implements GenericGameAspectFactory<Event> {

    final private GameRecordRepository recordRepository;

    public GameRecordAspectFactory(GameRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public GameAspect<Event> construct(GameConfiguration configuration, GameState state) {
        return new GameRecordEventAspect(state.getContext().getSessionKey(), recordRepository);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
