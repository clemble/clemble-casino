package com.clemble.casino.server.game.aspect.security;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;

import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.lifecycle.management.RoundGameContext;
import com.clemble.casino.game.lifecycle.management.event.GamePlayerMovedEvent;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.player.event.PlayerEvent;
import com.clemble.casino.server.game.aspect.GameAspect;

public class RoundGameSecurityAspect extends GameAspect<PlayerEvent> {

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
    public void doEvent(PlayerEvent event) {
        // Step 1. Checking player is one of participants
        if (!participants.contains(event.getPlayer()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayPlayerNotParticipate, event.getPlayer());
        // Step 2. Checking action was actually made
        if (context.getActionLatch().acted(event.getPlayer()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayMoveAlreadyMade, event.getPlayer());
    }

}
