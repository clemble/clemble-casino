package com.clemble.casino.server.game.aspect.price;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.account.GameAccount;
import com.clemble.casino.game.account.GamePlayerAccount;
import com.clemble.casino.game.action.BetAction;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.action.surrender.SurrenderAction;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.game.aspect.BasicGameAspect;
import com.clemble.casino.server.game.aspect.GameAspect;

public class GamePriceAspect extends BasicGameAspect {

    final private GameAccount account;

    public GamePriceAspect(GameAccount account) {
        super(new EventTypeSelector(BetAction.class));
        this.account = account;
    }

    @Override
    public void doEvent(Event move) {
        GamePlayerAccount gamePlayerState = account.getPlayerAccount(((PlayerAware) move).getPlayer());
        if (((BetAction) move).getBet() > gamePlayerState.getLeft())
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayBetOverflow);
    }

}
