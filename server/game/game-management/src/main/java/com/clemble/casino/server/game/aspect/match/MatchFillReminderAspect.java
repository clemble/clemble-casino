package com.clemble.casino.server.game.aspect.match;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.lifecycle.management.GamePlayerAccount;
import com.clemble.casino.game.lifecycle.management.GamePlayerContext;
import com.clemble.casino.game.lifecycle.management.MatchGameContext;
import com.clemble.casino.game.lifecycle.management.event.RoundEndedEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspect;

public class MatchFillReminderAspect extends MatchGameAspect<RoundEndedEvent> {

    final private MatchGameContext context;

    public MatchFillReminderAspect(MatchGameContext context) {
        super(new EventTypeSelector(RoundEndedEvent.class));
        this.context = context;
    }

    @Override
    protected void doEvent(RoundEndedEvent event) {
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
