package com.gogomaya.server.game.rule.giveup;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.action.GameEngine;
import com.gogomaya.server.game.action.GamePlayerState;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.impl.AbstractGameJudje;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.action.move.GiveUpMove;

public class GiveUpJudje<S extends GameState<?, ?>, M extends GameMove> extends AbstractGameJudje<S, M> {

    final byte percent;

    protected GiveUpJudje(final GameEngine<S, M> engine, final GiveUpRule giveUpRule) {
        super(engine, giveUpRule);

        switch (giveUpRule) {
        case all:
            this.percent = 100;
            break;
        case half:
            this.percent = 50;
            break;
        case lost:
            this.percent = 0;
            break;
        case quarter:
            this.percent = 25;
            break;
        case tenth:
            this.percent = 10;
            break;
        case therd:
            this.percent = 33;
            break;
        default:
            this.percent = 0;
            break;
        }
    }

    @Override
    public S process(S oldState, M gameMove) {
        if (gameMove instanceof GiveUpMove) {
            GamePlayerState playerState = oldState.getPlayerState(gameMove.getPlayerId());
            return oldState;
        } else {
            return proceed(oldState, gameMove);
        }
    }

    public static <S extends GameState<?, ?>, M extends GameMove> GameEngine<S, M> create(final GameEngine<S, M> engine, final GiveUpRule giveUpRule) {
        checkNotNull(giveUpRule);
        checkNotNull(engine);

        return new GiveUpJudje<S, M>(engine, giveUpRule);
    }

}
