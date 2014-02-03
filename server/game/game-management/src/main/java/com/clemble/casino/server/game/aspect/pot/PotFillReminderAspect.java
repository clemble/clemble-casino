package com.clemble.casino.server.game.aspect.pot;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GamePlayerAccount;
import com.clemble.casino.game.MatchGamePlayerContext;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.event.server.GameMatchEndedEvent;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class PotFillReminderAspect extends BasicGameAspect<GameMatchEndedEvent<?>>{

    final private PotGameContext potGameContext;

    public PotFillReminderAspect(PotGameContext context) {
        super(new EventTypeSelector(GameMatchEndedEvent.class));
        this.potGameContext = context;
    }

    @Override
    public void doEvent(GameMatchEndedEvent<?> event) {
        // Step 1. Filling pot with the reminder
        for (MatchGamePlayerContext playerContext : event.getState().getContext().getPlayerContexts()) {
            GamePlayerAccount playerMatchAccount = playerContext.getAccount();
            GamePlayerAccount playerPotAccount = potGameContext.get(playerContext.getPlayer()).getAccount();
            playerPotAccount.subLeft(playerMatchAccount.getSpent() + playerPotAccount.getLeft());
            playerPotAccount.addOwned(playerMatchAccount.getOwned());
            potGameContext.add(playerPotAccount.getLeft());
        }
    }



}
