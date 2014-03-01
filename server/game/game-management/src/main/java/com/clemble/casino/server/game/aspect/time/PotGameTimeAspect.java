package com.clemble.casino.server.game.aspect.time;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GamePlayerContext;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.PotGamePlayerContext;
import com.clemble.casino.game.event.server.RoundEndedEvent;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class PotGameTimeAspect extends BasicGameAspect<RoundEndedEvent> {

    final private PotGameContext context;

    public PotGameTimeAspect(PotGameContext context) {
        super(new EventTypeSelector(RoundEndedEvent.class));
        this.context = context;
    }

    @Override
    public void doEvent(RoundEndedEvent event) {
        for (GamePlayerContext playerContext : event.getState().getContext().getPlayerContexts()) {
            PotGamePlayerContext potPlayerContext = this.context.getPlayerContext(playerContext.getPlayer());
            // TODO Get rid of this small hack to change the pot clocks
            potPlayerContext.getClock().markToMove(-playerContext.getClock().getTimeSpent());
            potPlayerContext.getClock().markMoved();
        }
    }

}
