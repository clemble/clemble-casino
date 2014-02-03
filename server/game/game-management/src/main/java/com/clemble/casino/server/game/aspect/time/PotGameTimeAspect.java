package com.clemble.casino.server.game.aspect.time;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.MatchGamePlayerContext;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.PotGamePlayerContext;
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
        for (MatchGamePlayerContext playerContext : matchContext.getPlayerContexts()) {
            PotGamePlayerContext potPlayerContext = this.context.getPlayerContext(playerContext.getPlayer());
            // TODO Get rid of this small hack to change the pot clocks
            potPlayerContext.getClock().markToMove(-playerContext.getClock().getTimeSpent());
            potPlayerContext.getClock().markMoved();
        }
    }

}
