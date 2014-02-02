package com.clemble.casino.server.game.aspect.pot;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GamePlayerContext;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.event.server.GameMatchEndedEvent;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class PotFillReminderAspect extends BasicGameAspect<GameMatchEndedEvent<?>>{

    final private PotGameContext potGameContext;

    public PotFillReminderAspect(PotGameContext context) {
        super(new EventTypeSelector(GameMatchEndedEvent.class));
        this.potGameContext = context;
    }

    @Override
    public void doEvent(GameMatchEndedEvent<?> event) {
        // Step 1. Calculating max common
        for (GamePlayerContext playerContext : event.getState().getContext().getPlayerContexts())
            potGameContext.add(playerContext.getPlayer(), playerContext.getAccount().getLeft());
    }



}
