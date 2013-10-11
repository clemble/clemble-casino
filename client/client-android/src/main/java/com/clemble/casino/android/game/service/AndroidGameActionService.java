package com.clemble.casino.android.game.service;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.client.MadeMove;
import com.clemble.casino.web.game.GameWebMapping;
import com.clemble.casino.client.game.service.ClientGameActionService;
import com.clemble.casino.client.service.RestClientService;

public class AndroidGameActionService<State extends GameState> implements ClientGameActionService<State> {

    final private RestClientService restService;

    public AndroidGameActionService(RestClientService restService) {
        this.restService = checkNotNull(restService);
    }

    @Override
    @SuppressWarnings("unchecked")
    public State process(long sessionId, ClientEvent move) {
        return (State) restService.putForEntity(GameWebMapping.GAME_SESSIONS_ACTIONS, move, GameState.class, sessionId);
    }

    @Override
    public MadeMove getAction(long sessionId, int actionId) {
        return restService.getForEntity(GameWebMapping.GAME_SESSIONS_ACTIONS_ACTION, MadeMove.class, sessionId, actionId);
    }

    @Override
    public ClientGameActionService<State> clone(String server) {
        return new AndroidGameActionService<>(restService.construct(server));
    }

}