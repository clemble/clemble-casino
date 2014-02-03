package com.clemble.casino.server.game.aspect.pot;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GamePlayerAccount;
import com.clemble.casino.game.MatchGamePlayerContext;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.event.server.GameMatchEndedEvent;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class PotFillMaxCommonAspect extends BasicGameAspect<GameMatchEndedEvent<?>>{

    final private PotGameContext potGameContext;

    public PotFillMaxCommonAspect(PotGameContext context) {
        super(new EventTypeSelector(GameMatchEndedEvent.class));
        this.potGameContext = context;
    }

    @Override
    public void doEvent(GameMatchEndedEvent<?> event) {
        // Step 1. Calculating max common
        long maxCommon = Integer.MAX_VALUE;
        for (MatchGamePlayerContext playerContext : event.getState().getContext().getPlayerContexts())
            maxCommon = Math.min(playerContext.getAccount().getLeft(), maxCommon);
        // Step 2. Adding max common to each player's pot value
        for (MatchGamePlayerContext playerContext : event.getState().getContext().getPlayerContexts()) {
            GamePlayerAccount playerMatchAccount = playerContext.getAccount();
            GamePlayerAccount playerPotAccount = potGameContext.getPlayerContext(playerContext.getPlayer()).getAccount();
            playerPotAccount.subLeft(playerMatchAccount.getSpent() + maxCommon);
            playerPotAccount.addOwned(playerMatchAccount.getOwned());
            potGameContext.add(maxCommon);
        }

    }

}
