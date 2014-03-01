package com.clemble.casino.server.game.aspect.pot;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GamePlayerAccount;
import com.clemble.casino.game.GamePlayerContext;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.event.server.RoundEndedEvent;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class PotFillNoneAspect extends BasicGameAspect<RoundEndedEvent>{

    final private PotGameContext context;

    public PotFillNoneAspect(PotGameContext context) {
        super(new EventTypeSelector(RoundEndedEvent.class));
        this.context = context;
    }

    @Override
    public void doEvent(RoundEndedEvent event) {
        // Step 1. Making adjustment to the player accounts in pot
        for (GamePlayerContext playerContext : event.getState().getContext().getPlayerContexts()) {
            GamePlayerAccount playerMatchAccount = playerContext.getAccount();
            GamePlayerAccount playerPotAccount = context.getPlayerContext(playerContext.getPlayer()).getAccount();
            playerPotAccount.subLeft(playerMatchAccount.getSpent());
            playerPotAccount.addOwned(playerMatchAccount.getOwned());
        }
    }

}