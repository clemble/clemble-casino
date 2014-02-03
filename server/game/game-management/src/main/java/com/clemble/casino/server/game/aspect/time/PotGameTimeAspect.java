package com.clemble.casino.server.game.aspect.time;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GamePlayerContext;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.PotPlayerGameContext;
import com.clemble.casino.game.event.server.GameMatchEndedEvent;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class PotGameTimeAspect extends BasicGameAspect<GameMatchEndedEvent<?>> {

    final private PotGameContext context;

    public PotGameTimeAspect(PotGameContext context) {
        super(new EventTypeSelector(GameMatchEndedEvent.class));
        this.context = context;
    }

    @Override
    public void doEvent(GameMatchEndedEvent<?> event) {
        MatchGameContext matchContext = event.getState().getContext();
        for (GamePlayerContext playerContext : matchContext.getPlayerContexts()) {
            PotPlayerGameContext potPlayerContext = this.context.get(playerContext.getPlayer());
            // TODO Get rid of this small hack to change the pot clocks
            potPlayerContext.getClock().markToMove(-playerContext.getClock().getTimeSpent());
            potPlayerContext.getClock().markMoved();
        }
    }
}
