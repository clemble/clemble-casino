package com.clemble.casino.server.game.aspect.price;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.account.GamePlayerAccount;
import com.clemble.casino.game.action.BetAction;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class GamePriceAspect extends BasicGameAspect {

    final private GameContext account;

    public GamePriceAspect(GameContext account) {
        super(new EventTypeSelector(BetAction.class));
        this.account = account;
    }

    @Override
    public void doEvent(Event move) {
        GamePlayerAccount gamePlayerState = account.getPlayerContext(((PlayerAware) move).getPlayer()).getAccount();
        if (((BetAction) move).getBet() > gamePlayerState.getLeft())
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayBetOverflow);
    }

}
