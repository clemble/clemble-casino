package com.clemble.casino.server.game.aspect.time;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.lifecycle.management.GamePlayerContext;
import com.clemble.casino.game.lifecycle.management.MatchGameContext;
import com.clemble.casino.game.lifecycle.management.MatchGamePlayerContext;
import com.clemble.casino.game.lifecycle.management.event.RoundEndedEvent;
import com.clemble.casino.server.game.aspect.GameAspect;

public class MatchGameTimeAspect extends GameAspect<RoundEndedEvent> {

    final private MatchGameContext context;

    public MatchGameTimeAspect(MatchGameContext context) {
        super(new EventTypeSelector(RoundEndedEvent.class));
        this.context = context;
    }

    @Override
    protected void doEvent(RoundEndedEvent event) {
        for (GamePlayerContext playerContext : event.getState().getContext().getPlayerContexts()) {
            MatchGamePlayerContext matchPlayerContext = this.context.getPlayerContext(playerContext.getPlayer());
            // TODO Get rid of this small hack to change the match clocks
            matchPlayerContext.getClock().deduce(playerContext.getClock().getTimeSpent());
            matchPlayerContext.getClock().stop();
        }
    }

}
