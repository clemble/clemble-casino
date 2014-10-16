package com.clemble.casino.server.game.aspect.unit;

import com.clemble.casino.client.PlayerActionTypeSelector;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.lifecycle.management.GameContext;
import com.clemble.casino.game.lifecycle.management.event.action.PlayerGameAction;
import com.clemble.casino.game.lifecycle.management.event.action.UseGameUnitAction;
import com.clemble.casino.lifecycle.management.event.action.PlayerAction;
import com.clemble.casino.server.game.aspect.GameAspect;

/**
 * Created by mavarazy on 15/03/14.
 */
public class GamePlayerUnitAspect extends GameAspect<PlayerAction<UseGameUnitAction>> {

    final private GameContext<?> context;

    public GamePlayerUnitAspect(GameContext context) {
        super(new PlayerActionTypeSelector(UseGameUnitAction.class));
        this.context = context;
    }

    @Override
    protected void doEvent(PlayerAction<UseGameUnitAction> event) {
        UseGameUnitAction action = event.getAction();
        if (!context.getPlayerContext(event.getPlayer()).getUnits().contains(action.getUnit()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayUnitMissing, event.getPlayer());
    }

}
