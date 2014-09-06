package com.clemble.casino.server.game.aspect.unit;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.action.UseGameUnitEvent;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

/**
 * Created by mavarazy on 15/03/14.
 */
public class GamePlayerUnitAspect extends BasicGameAspect<UseGameUnitEvent> {

    final private GameContext<?> context;

    public GamePlayerUnitAspect(GameContext context) {
        super(new EventTypeSelector(UseGameUnitEvent.class));
        this.context = context;
    }

    @Override
    public void doEvent(UseGameUnitEvent event) {
        if (!context.getPlayerContext(event.getPlayer()).getUnits().contains(event.getUnit()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayUnitMissing);
    }
}
