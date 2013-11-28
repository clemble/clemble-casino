package com.clemble.casino.android.game.service;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import org.springframework.web.client.RestTemplate;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.android.AbstractClembleCasinoOperations;
import com.clemble.casino.client.game.ClientGameActionOperations;
import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.client.MadeMove;
import com.clemble.casino.web.game.GameWebMapping;

public class AndroidGameActionTemplate<State extends GameState> extends AbstractClembleCasinoOperations implements ClientGameActionOperations<State> {

    final private RestTemplate restTemplate;

    public AndroidGameActionTemplate(ServerRegistry apiBase, RestTemplate restTemplate) {
        super(apiBase);
        this.restTemplate = checkNotNull(restTemplate);
    }

    @Override
    @SuppressWarnings("unchecked")
    public State getState(Game game, String session) {
        return (State) restTemplate
                .getForEntity(buildUriWith(GameWebMapping.GAME_SESSIONS_STATE, game, session), GameState.class)
                .getBody();
    }

    @Override
    @SuppressWarnings("unchecked")
    public State process(Game game, String session, ClientEvent move) {
        return (State) restTemplate
            .postForEntity(buildUriWith(GameWebMapping.GAME_SESSIONS_ACTIONS, game, session), move, GameState.class)
            .getBody();
    }

    @Override
    public MadeMove getAction(Game game, String session, int action) {
        return restTemplate
            .getForEntity(buildUriWith(GameWebMapping.GAME_SESSIONS_ACTIONS_ACTION, game, session, action), MadeMove.class)
            .getBody();
    }

}
