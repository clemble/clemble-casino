package com.clemble.casino.server.game.aspect.security;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;

import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.PlayerAwareEvent;
import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.event.server.PlayerMovedEvent;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.game.aspect.GameAspect;

public class RoundGameSecurityAspect extends GameAspect<PlayerAwareEvent> {

    final private RoundGameContext context;
    final private Collection<String> participants;

    public RoundGameSecurityAspect(RoundGameContext context, Collection<String> participants) {
        // This is on purpose to filter messages to detect messages that might be PlayerAware, but not PlayerAwareEvent
        super(EventSelectors
            .where(new EventTypeSelector(PlayerAware.class))
            .and(EventSelectors.not(new EventTypeSelector(PlayerMovedEvent.class))));
        this.context = context;
        this.participants = new ArrayList<>(checkNotNull(participants));
    }

    @Override
    public void doEvent(PlayerAwareEvent event) {
        // Step 1. Checking player is one of participants
        if (!participants.contains(event.getPlayer()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayPlayerNotParticipate);
        // Step 2. Checking action was actually made
        if (context.getActionLatch().acted(event.getPlayer()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayMoveAlreadyMade);
    }

}
