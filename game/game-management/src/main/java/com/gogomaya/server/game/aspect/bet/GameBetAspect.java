package com.gogomaya.server.game.aspect.bet;

import java.util.Collection;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.aspect.GameAspect;
import com.gogomaya.server.game.event.client.BetEvent;
import com.gogomaya.server.game.event.server.GameServerEvent;
import com.gogomaya.server.game.rule.bet.BetRule;

public class GameBetAspect<State extends GameState> implements GameAspect<State> {

    final private BetRule betRule;

    public GameBetAspect(BetRule betRule) {
        this.betRule = betRule;
    }

    @Override
    public void beforeMove(GameSession<State> session, ClientEvent move) {
        if (move instanceof BetEvent) {
            if (!betRule.isValid((BetEvent) move)) {
                throw GogomayaException.fromError(GogomayaError.GamePlayBetInvalid);
            }
        }
    }

    @Override
    public Collection<GameServerEvent<State>> afterMove(GameSession<State> session, Collection<GameServerEvent<State>> events) {
        return events;
    }

    @Override
    public Collection<GameServerEvent<State>> afterGame(GameSession<State> session, Collection<GameServerEvent<State>> events) {
        return events;
    }

}
