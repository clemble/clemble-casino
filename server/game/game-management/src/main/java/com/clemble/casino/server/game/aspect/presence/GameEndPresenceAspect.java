package com.clemble.casino.server.game.aspect.presence;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.event.server.RoundEndedEvent;
import com.clemble.casino.server.game.aspect.BasicGameAspect;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;

public class GameEndPresenceAspect extends BasicGameAspect<RoundEndedEvent> {

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
