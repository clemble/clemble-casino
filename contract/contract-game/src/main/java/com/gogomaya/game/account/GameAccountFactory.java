package com.gogomaya.game.account;

import java.util.ArrayList;
import java.util.List;

import com.gogomaya.game.construct.GameInitiation;
import com.gogomaya.game.rule.visibility.VisibilityRule;
import com.gogomaya.game.specification.GameSpecification;
import com.gogomaya.money.Currency;
import com.gogomaya.money.Money;

public class GameAccountFactory {

    public static GameAccount create(GameInitiation initiation) {
        GameSpecification specification = initiation.getSpecification();
        List<Long> players = new ArrayList<>(initiation.getParticipants());
        // Step 1. Generating player accounts
        List<GamePlayerAccount> playerAccounts = new ArrayList<>(players.size());
        long amount = specification.getPrice().getAmount();
        for (Long player : players) {
            playerAccounts.add(new GamePlayerAccount(player, amount));
        }
        return specification.getVisibilityRule() == VisibilityRule.hidden ? new InvisibleGameAccount(Money.create(Currency.FakeMoney, 0), playerAccounts)
                : new VisibleGameAccount(Money.create(Currency.FakeMoney, 0), playerAccounts);
    }

}
