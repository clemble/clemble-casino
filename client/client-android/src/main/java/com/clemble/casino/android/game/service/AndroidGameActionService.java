package com.clemble.casino.android.game.service;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.client.game.ClientGameActionOperations;
import com.clemble.casino.client.service.RestClientService;
import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.client.MadeMove;
import com.clemble.casino.web.game.GameWebMapping;

public class AndroidGameActionService<State extends GameState> implements ClientGameActionOperations<State> {

    final private RestClientService restService;

    public AndroidGameActionService(RestClientService restService) {
        this.restService = checkNotNull(restService);
    }

    @Override
    @SuppressWarnings("unchecked")
    public State process(String sessionId, ClientEvent move) {
        return (State) restService.putForEntity(GameWebMapping.GAME_SESSIONS_ACTIONS, move, GameState.class, sessionId);
    }

    @Override
    public MadeMove getAction(String sessionId, int actionId) {
        return restService.getForEntity(GameWebMapping.GAME_SESSIONS_ACTIONS_ACTION, MadeMove.class, sessionId, actionId);
    }

}
