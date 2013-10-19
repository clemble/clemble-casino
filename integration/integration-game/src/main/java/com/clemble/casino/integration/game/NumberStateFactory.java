package com.clemble.casino.integration.game;

import java.util.ArrayList;
import java.util.Collection;

import com.clemble.casino.base.ActionLatch;
import com.clemble.casino.game.account.GameAccount;
import com.clemble.casino.game.account.GamePlayerAccount;
import com.clemble.casino.game.account.VisibleGameAccount;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.iterator.SequentialPlayerIterator;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.server.game.action.GameProcessorFactory;
import com.clemble.casino.server.game.action.impl.AbstractGameStateFactory;
import com.clemble.casino.server.repository.game.GameConstructionRepository;

public class NumberStateFactory extends AbstractGameStateFactory<NumberState> {

    public NumberStateFactory(GameConstructionRepository constructionRepository, GameProcessorFactory<NumberState> processorFactory) {
        super(constructionRepository, processorFactory);
    }

    @Override
    public NumberState constructState(GameInitiation initiation) {
        SequentialPlayerIterator playerIterator = new SequentialPlayerIterator(initiation.getParticipants());
        ActionLatch expectedActions = new ActionLatch(initiation.getParticipants(), "selectNumber");

        Money price = initiation.getSpecification().getPrice();
        Collection<GamePlayerAccount> playerAccounts = new ArrayList<>();
        for (String participant : initiation.getParticipants()) {
            playerAccounts.add(new GamePlayerAccount(participant, price.getAmount()));
        }

        GameAccount gameAccount = new VisibleGameAccount(price, playerAccounts);
        return new NumberState(expectedActions, gameAccount, playerIterator);
    }

}
