package com.clemble.casino.server.game.aspect.security;

import java.util.Collection;

import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.lifecycle.management.RoundGameContext;
import com.clemble.casino.game.lifecycle.management.event.GamePlayerMovedEvent;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.game.aspect.GameAspect;

public class RoundGameSecurityAspect<T extends Event & PlayerAware> extends GameAspect<T> {

    final private RoundGameContext context;
    final private Collection<String> participants;

    public RoundGameSecurityAspect(RoundGameContext context) {
        // This is on purpose to filter messages to detect messages that might be PlayerAware, but not PlayerAwareEvent
        super(EventSelectors
            .where(new EventTypeSelector(PlayerAware.class))
            .and(EventSelectors.not(new EventTypeSelector(GamePlayerMovedEvent.class))));
        this.context = context;
        this.participants = PlayerAwareUtils.toPlayerList(context.getPlayerContexts());
    }

    @Override
    protected void doEvent(T event) {
        String player = event.getPlayer();
        // Step 1. Checking player is one of participants
        if (!participants.contains(player))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayPlayerNotParticipate, player);
        // Step 2. Checking action was actually made
        if (context.getActionLatch().acted(player))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayMoveAlreadyMade, player);
    }

}
