package com.clemble.casino.server.game.aspect.bet;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.bet.PlayerBetAction;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GamePlayerAccount;
import com.clemble.casino.rule.bet.BetRule;
import com.clemble.casino.server.game.aspect.GameAspect;

public class GameBetRuleAspect extends GameAspect<PlayerBetAction> {

    final private BetRule betRule;
    final private GameContext<?> context;

    public GameBetRuleAspect(GameContext<?> context, BetRule betRule) {
        super(new EventTypeSelector(PlayerBetAction.class));
        this.context = context;
        this.betRule = betRule;
    }

    @Override
    public void doEvent(PlayerBetAction bet) {
        // Step 1. Checking bet is valid, according to the account
        if (!betRule.isValid(bet))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayBetInvalid, bet.getPlayer());
        // Step 2. Checking player has needed amount
        GamePlayerAccount playerAccount = context.getPlayerContext(bet.getPlayer()).getAccount();
        if (!playerAccount.canAfford(bet.getBet()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayBetOverflow, bet.getPlayer());
    }

}
