package com.clemble.casino.server.game.aspect.presence;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.server.game.aspect.BasicGameAspect;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;

public class GameEndPresenceAspect extends BasicGameAspect<GameEndedEvent<?>> {

    final private PlayerPresenceServerService presenceService;

    public GameEndPresenceAspect(PlayerPresenceServerService presenceService) {
        super(new EventTypeSelector(GameEndedEvent.class));
        this.presenceService = checkNotNull(presenceService);
    }

    @Override
    public void doEvent(GameEndedEvent<?> event) {
        for (String player : event.getState().getContext().getPlayerIterator().getPlayers())
            presenceService.markOnline(player);
    }

}
