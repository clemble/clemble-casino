package com.gogomaya.server.game.account;

import java.util.ArrayList;
import java.util.List;

import com.gogomaya.server.game.construct.GameInitiation;
import com.gogomaya.server.game.rule.visibility.VisibilityRule;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.money.Currency;
import com.gogomaya.server.money.Money;

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
