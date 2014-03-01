package com.clemble.casino.server.game.aspect.pot;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GamePlayerAccount;
import com.clemble.casino.game.GamePlayerContext;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.event.server.RoundEndedEvent;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class MatchFillMaxCommonAspect extends BasicGameAspect<RoundEndedEvent>{

    final private MatchGameContext context;

    public MatchFillMaxCommonAspect(MatchGameContext context) {
        super(new EventTypeSelector(RoundEndedEvent.class));
        this.context = context;
    }

    @Override
    public void doEvent(RoundEndedEvent event) {
        // Step 1. Calculating max common
        long maxCommon = Integer.MAX_VALUE;
        for (GamePlayerContext playerContext : event.getState().getContext().getPlayerContexts())
            maxCommon = Math.min(playerContext.getAccount().getLeft(), maxCommon);
        // Step 2. Adding max common to each player's pot value
        for (GamePlayerContext playerContext : event.getState().getContext().getPlayerContexts()) {
            GamePlayerAccount playerMatchAccount = playerContext.getAccount();
            GamePlayerAccount playerPotAccount = context.getPlayerContext(playerContext.getPlayer()).getAccount();
            playerPotAccount.subLeft(playerMatchAccount.getSpent() + maxCommon);
            playerPotAccount.addOwned(playerMatchAccount.getOwned());
            context.add(maxCommon);
        }

    }

}
