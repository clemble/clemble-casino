package com.clemble.casino.server.game.aspect.time;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GamePlayerContext;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.MatchGamePlayerContext;
import com.clemble.casino.game.event.server.RoundEndedEvent;
import com.clemble.casino.server.game.aspect.GameAspect;

public class MatchGameTimeAspect extends GameAspect<RoundEndedEvent> {

    final private MatchGameContext context;

    public MatchGameTimeAspect(MatchGameContext context) {
        super(new EventTypeSelector(RoundEndedEvent.class));
        this.context = context;
    }

    @Override
    public void doEvent(RoundEndedEvent event) {
        for (GamePlayerContext playerContext : event.getState().getContext().getPlayerContexts()) {
            MatchGamePlayerContext potPlayerContext = this.context.getPlayerContext(playerContext.getPlayer());
            // TODO Get rid of this small hack to change the pot clocks
            potPlayerContext.getClock().markToMove(- playerContext.getClock().getTimeSpent());
            potPlayerContext.getClock().markMoved();
        }
    }

}
