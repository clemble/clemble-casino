package com.gogomaya.android.game.service;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import com.gogomaya.client.service.RestClientService;
import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameState;
import com.gogomaya.game.event.client.MadeMove;
import com.gogomaya.game.service.GameActionService;
import com.gogomaya.web.game.GameWebMapping;

public class AndroidGameActionService<State extends GameState> implements GameActionService<State> {

    final private RestClientService restService;

    public AndroidGameActionService(RestClientService restService) {
        this.restService = checkNotNull(restService);
    }

    @Override
    @SuppressWarnings("unchecked")
    public State process(long sessionId, ClientEvent move) {
        return (State) restService.putForEntity(GameWebMapping.GAME_PREFIX, GameWebMapping.GAME_SESSION_ACTIONS, move, GameState.class, sessionId);
    }

    @Override
    public MadeMove getAction(long sessionId, int actionId) {
        return restService.getForEntity(GameWebMapping.GAME_PREFIX, GameWebMapping.GAME_SESSION_ACTIONS_ACTION, MadeMove.class, sessionId, actionId);
    }

}
