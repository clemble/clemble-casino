package com.clemble.casino.server.game.aspect.pot;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GamePlayerAccount;
import com.clemble.casino.game.MatchGamePlayerContext;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.event.server.GameMatchEndedEvent;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class PotFillNoneAspect extends BasicGameAspect<GameMatchEndedEvent<?>>{

    final private PotGameContext potGameContext;

    public PotFillNoneAspect(PotGameContext context) {
        super(new EventTypeSelector(GameMatchEndedEvent.class));
        this.potGameContext = context;
    }

    @Override
    public void doEvent(GameMatchEndedEvent<?> event) {
        // Step 1. Making adjustment to the player accounts in pot
        for (MatchGamePlayerContext playerContext : event.getState().getContext().getPlayerContexts()) {
            GamePlayerAccount playerMatchAccount = playerContext.getAccount();
            GamePlayerAccount playerPotAccount = potGameContext.getPlayerContext(playerContext.getPlayer()).getAccount();
            playerPotAccount.subLeft(playerMatchAccount.getSpent());
            playerPotAccount.addOwned(playerMatchAccount.getOwned());
        }
    }

}