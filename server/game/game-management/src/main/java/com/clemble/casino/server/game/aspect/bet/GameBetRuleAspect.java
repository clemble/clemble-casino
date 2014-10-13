package com.clemble.casino.server.game.aspect.bet;

import com.clemble.casino.client.PlayerActionTypeSelector;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.lifecycle.management.event.action.PlayerAction;
import com.clemble.casino.lifecycle.management.event.action.bet.BetAction;
import com.clemble.casino.game.lifecycle.management.GameContext;
import com.clemble.casino.game.lifecycle.management.GamePlayerAccount;
import com.clemble.casino.lifecycle.configuration.rule.bet.BetRule;
import com.clemble.casino.server.game.aspect.GameAspect;

public class GameBetRuleAspect extends GameAspect<PlayerAction<BetAction>> {

    final private BetRule betRule;
    final private GameContext<?> context;

    public GameBetRuleAspect(GameContext<?> context, BetRule betRule) {
        super(new PlayerActionTypeSelector(BetAction.class));
        this.context = context;
        this.betRule = betRule;
    }

    @Override
    public void doEvent(PlayerAction<BetAction> bet) {
        BetAction betAction = bet.getAction();
        // Step 1. Checking bet is valid, according to the account
        if (!betRule.isValid(betAction))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayBetInvalid, bet.getPlayer());
        // Step 2. Checking player has needed amount
        GamePlayerAccount playerAccount = context.getPlayerContext(bet.getPlayer()).getAccount();
        if (!playerAccount.canAfford(betAction.getBet()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayBetOverflow, bet.getPlayer());
    }

}
