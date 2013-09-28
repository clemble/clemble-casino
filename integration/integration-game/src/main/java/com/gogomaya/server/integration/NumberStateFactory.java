package com.gogomaya.server.integration;

import java.util.ArrayList;
import java.util.Collection;

import com.gogomaya.base.ActionLatch;
import com.gogomaya.game.account.GameAccount;
import com.gogomaya.game.account.GamePlayerAccount;
import com.gogomaya.game.account.VisibleGameAccount;
import com.gogomaya.game.construct.GameInitiation;
import com.gogomaya.game.iterator.SequentialPlayerIterator;
import com.gogomaya.money.Money;
import com.gogomaya.server.game.action.GameProcessorFactory;
import com.gogomaya.server.game.action.impl.AbstractGameStateFactory;
import com.gogomaya.server.repository.game.GameConstructionRepository;

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
