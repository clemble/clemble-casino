package com.clemble.casino.server.game.aspect.price;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.GamePlayerAccount;
import com.clemble.casino.game.action.BetAction;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class GamePriceAspect extends BasicGameAspect<BetAction> {

    final private MatchGameContext context;

    public GamePriceAspect(MatchGameContext account) {
        super(new EventTypeSelector(BetAction.class));
        this.context = account;
    }

    @Override
    public void doEvent(BetAction move) {
        GamePlayerAccount gamePlayerState = context.getPlayerContext(move.getPlayer()).getAccount();
        if (move.getBet() > gamePlayerState.getLeft())
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayBetOverflow);
    }

}
