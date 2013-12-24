package com.clemble.casino.server.game.aspect.presence;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameSessionState;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.game.aspect.BasicGameManagementAspect;
import com.clemble.casino.server.game.aspect.GameManagementAspect;
import com.clemble.casino.server.game.aspect.GameManagementAspecteFactory;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;

import java.util.Collection;

/**
 * Created by mavarazy on 23/12/13.
 */
public class GamePresenceAspectFactory implements GameManagementAspecteFactory {

    private final PlayerPresenceServerService presenceService;

    final private GameManagementAspect presenceAspect = new BasicGameManagementAspect() {
        @Override
        public <State extends GameState> void changed(State state, Collection<Event> events) {

        }

        @Override
        public <State extends GameState> void afterGame(GameSession<State> session) {
            session.setSessionState(GameSessionState.finished);
            for (String player : session.getState().getContext().getPlayerIterator().getPlayers())
                presenceService.markOnline(player);
        }
    };

    public GamePresenceAspectFactory(PlayerPresenceServerService presenceService) {
        this.presenceService = presenceService;
    }

    @Override
    public GameManagementAspect construct(GameInitiation initiation) {
        return presenceAspect;
    }
}
