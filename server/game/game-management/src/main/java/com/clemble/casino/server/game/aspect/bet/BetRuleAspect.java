package com.clemble.casino.server.game.aspect.bet;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GamePlayerAccount;
import com.clemble.casino.event.bet.BetEvent;
import com.clemble.casino.rule.bet.BetRule;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class BetRuleAspect extends BasicGameAspect<BetEvent> {

    final private BetRule betRule;
    final private GameContext<?> context;

    public BetRuleAspect(GameContext<?> context, BetRule betRule) {
        super(new EventTypeSelector(BetEvent.class));
        this.context = context;
        this.betRule = betRule;
    }

    @Override
    public void doEvent(BetEvent bet) {
        // Step 1. Checking bet is valid, according to the account
        if (!betRule.isValid(bet))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayBetInvalid);
        // Step 2. Checking player has needed amount
        GamePlayerAccount playerAccount = context.getPlayerContext(bet.getPlayer()).getAccount();
        if (!playerAccount.canAfford(bet.getBet()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayBetOverflow);
    }

}
