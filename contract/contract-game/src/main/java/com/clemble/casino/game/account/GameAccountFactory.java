package com.clemble.casino.game.account;

import java.util.ArrayList;
import java.util.List;

import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.rule.visibility.VisibilityRule;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;

public class GameAccountFactory {

    public static GameAccount create(GameInitiation initiation) {
        GameSpecification specification = initiation.getSpecification();
        List<String> players = new ArrayList<>(initiation.getParticipants());
        // Step 1. Generating player accounts
        List<GamePlayerAccount> playerAccounts = new ArrayList<>(players.size());
        long amount = specification.getPrice().getAmount();
        for (String player : players) {
            playerAccounts.add(new GamePlayerAccount(player, amount));
        }
        return specification.getVisibilityRule() == VisibilityRule.hidden ? new InvisibleGameAccount(Money.create(Currency.FakeMoney, 0), playerAccounts)
                : new VisibleGameAccount(Money.create(Currency.FakeMoney, 0), playerAccounts);
    }

}
