package com.clemble.casino.server.game.aspect.security;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.PlayerAwareEvent;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class GameMatchSecurityAspect extends BasicGameAspect<PlayerAwareEvent> {

    final private Collection<String> participants;

    public GameMatchSecurityAspect(Collection<String> participants) {
        // This is on purpose to filter messages to detect messages that might be PlayerAware, but not PlayerAwareEvent
        super(new EventTypeSelector(PlayerAware.class));
        this.participants = new ArrayList<>(checkNotNull(participants));
    }

    @Override
    public void doEvent(PlayerAwareEvent event) {
        if (!participants.contains(event.getPlayer()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayPlayerNotParticipate);
    }

}
