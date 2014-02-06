package com.clemble.casino.server.game.aspect.pot;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GamePlayerAccount;
import com.clemble.casino.game.GamePlayerContext;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.event.server.GameMatchEndedEvent;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class PotFillReminderAspect extends BasicGameAspect<GameMatchEndedEvent>{

    final private PotGameContext context;

    public PotFillReminderAspect(PotGameContext context) {
        super(new EventTypeSelector(GameMatchEndedEvent.class));
        this.context = context;
    }

    @Override
    public void doEvent(GameMatchEndedEvent event) {
        // Step 1. Filling pot with the reminder
        for (GamePlayerContext playerContext : event.getState().getContext().getPlayerContexts()) {
            GamePlayerAccount playerMatchAccount = playerContext.getAccount();
            GamePlayerAccount playerPotAccount = context.getPlayerContext(playerContext.getPlayer()).getAccount();
            playerPotAccount.subLeft(playerMatchAccount.getSpent() + playerPotAccount.getLeft());
            playerPotAccount.addOwned(playerMatchAccount.getOwned());
            context.add(playerPotAccount.getLeft());
        }
    }



}
