package com.clemble.casino.server.game.aspect.match;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.lifecycle.management.GamePlayerAccount;
import com.clemble.casino.game.lifecycle.management.GamePlayerContext;
import com.clemble.casino.game.lifecycle.management.MatchGameContext;
import com.clemble.casino.game.lifecycle.management.event.RoundEndedEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspect;

public class MatchFillMaxCommonAspect extends MatchGameAspect<RoundEndedEvent> {

    final private MatchGameContext context;

    public MatchFillMaxCommonAspect(MatchGameContext context) {
        super(new EventTypeSelector(RoundEndedEvent.class));
        this.context = context;
    }

    @Override
    protected void doEvent(RoundEndedEvent event) {
        // Step 1. Calculating max common
        long maxCommon = Integer.MAX_VALUE;
        for (GamePlayerContext playerContext : event.getState().getContext().getPlayerContexts())
            maxCommon = Math.min(playerContext.getAccount().getLeft(), maxCommon);
        // Step 2. Adding max common to each player's pot value
        for (GamePlayerContext playerContext : event.getState().getContext().getPlayerContexts()) {
            GamePlayerAccount playerRoundAccount = playerContext.getAccount();
            GamePlayerAccount playerMatchAccount = context.getPlayerContext(playerContext.getPlayer()).getAccount();
            playerMatchAccount.subLeft(playerRoundAccount.getSpent() + maxCommon);
            playerMatchAccount.addOwned(playerRoundAccount.getOwned());
            context.add(maxCommon);
        }

    }

}
