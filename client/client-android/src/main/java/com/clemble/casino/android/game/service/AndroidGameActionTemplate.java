package com.clemble.casino.android.game.service;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import org.springframework.web.client.RestTemplate;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.android.AbstractClembleCasinoOperations;
import com.clemble.casino.client.game.ClientGameActionOperations;
import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.client.MadeMove;
import com.clemble.casino.web.game.GameWebMapping;

public class AndroidGameActionTemplate<State extends GameState> extends AbstractClembleCasinoOperations implements ClientGameActionOperations<State> {

    final private RestTemplate restTemplate;

    public AndroidGameActionTemplate(RestTemplate restTemplate, ServerRegistry apiBase) {
        super(apiBase);
        this.restTemplate = checkNotNull(restTemplate);
    }

    @Override
    @SuppressWarnings("unchecked")
    public State process(String sessionId, ClientEvent move) {
        return (State) restTemplate
            .postForEntity(buildUriWith(GameWebMapping.GAME_SESSIONS_ACTIONS, sessionId), move, GameState.class)
            .getBody();
    }

    @Override
    public MadeMove getAction(String sessionId, int actionId) {
        return restTemplate
            .getForEntity(buildUriWith(GameWebMapping.GAME_SESSIONS_ACTIONS_ACTION, sessionId, actionId), MadeMove.class)
            .getBody();
    }

}
