package com.gogomaya.server.tictactoe;

import java.util.ArrayList;
import java.util.List;

import com.gogomaya.server.game.GamePlayerIterator;
import com.gogomaya.server.game.SequentialPlayerIterator;
import com.gogomaya.server.game.action.GameProcessorFactory;
import com.gogomaya.server.game.action.impl.AbstractGameStateFactory;
import com.gogomaya.server.game.bank.GameBank;
import com.gogomaya.server.game.bank.GamePlayerAccount;
import com.gogomaya.server.game.bank.VisibleGameBank;
import com.gogomaya.server.game.construct.GameInitiation;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.money.Currency;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.repository.game.GameConstructionRepository;

public class PicPacPoeStateFactory extends AbstractGameStateFactory<PicPacPoeState>{

    public PicPacPoeStateFactory(GameConstructionRepository constructionRepository, GameProcessorFactory<PicPacPoeState> processorFactory) {
        super(constructionRepository, processorFactory);
    }

    @Override
    public PicPacPoeState constructState(GameInitiation initiation) {
        GameSpecification specification = initiation.getSpecification();
        List<Long> players = new ArrayList<>(initiation.getParticipants());
        // Step 1. Generating player accounts
        List<GamePlayerAccount> playerAccounts = new ArrayList<>(players.size());
        long amount = specification.getPrice().getAmount();
        for(Long player: players) {
            playerAccounts.add(new GamePlayerAccount(player, amount));
        }
        GameBank account = new VisibleGameBank(Money.create(Currency.FakeMoney, 0), playerAccounts);
        // Step 2. Processing generated accounts
        GamePlayerIterator playerIterator = new SequentialPlayerIterator(playerAccounts);
        // Step 3. 
        return new PicPacPoeState(account, playerIterator);
    }

}
