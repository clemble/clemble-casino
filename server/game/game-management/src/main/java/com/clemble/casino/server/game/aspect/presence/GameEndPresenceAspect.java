package com.clemble.casino.server.game.aspect.presence;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.lifecycle.management.event.RoundEndedEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;

public class GameEndPresenceAspect extends GameAspect<RoundEndedEvent> {

    final private ServerPlayerPresenceService presenceService;

    public GameEndPresenceAspect(ServerPlayerPresenceService presenceService) {
        super(new EventTypeSelector(RoundEndedEvent.class));
        this.presenceService = checkNotNull(presenceService);
    }

    @Override
    public void doEvent(RoundEndedEvent event) {
        for (String player : event.getState().getContext().getPlayerIterator().getPlayers())
            presenceService.markOnline(player);
    }

}
