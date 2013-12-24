package com.clemble.casino.server.game.aspect.security;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.game.aspect.BasicGameAspect;
import com.clemble.casino.server.game.aspect.GameAspect;

public class GameSecurityAspect extends BasicGameAspect {

    final private Collection<String> participants;

    public GameSecurityAspect(Collection<String> participants) {
        super(new EventTypeSelector(PlayerAware.class));
        this.participants = new ArrayList<>(checkNotNull(participants));
    }

    @Override
    public void doEvent(Event event) {
        if (!participants.contains(((PlayerAware) event).getPlayer()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayPlayerNotParticipate);
    }

}
