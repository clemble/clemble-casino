package com.gogomaya.server.tictactoe;

import java.util.ArrayList;
import java.util.List;

import com.gogomaya.server.game.GameAccount;
import com.gogomaya.server.game.GamePlayerAccount;
import com.gogomaya.server.game.GamePlayerIterator;
import com.gogomaya.server.game.SequentialPlayerIterator;
import com.gogomaya.server.game.action.GameProcessorFactory;
import com.gogomaya.server.game.action.impl.AbstractGameStateFactory;
import com.gogomaya.server.game.construct.GameInitiation;
import com.gogomaya.server.repository.game.GameConstructionRepository;

public class PicPacPoeStateFactory extends AbstractGameStateFactory<PicPacPoeState>{

    public PicPacPoeStateFactory(GameConstructionRepository constructionRepository, GameProcessorFactory<PicPacPoeState> processorFactory) {
        super(constructionRepository, processorFactory);
    }

    @Override
    public PicPacPoeState constructState(GameInitiation initiation) {
        List<Long> players = new ArrayList<>(initiation.getParticipants());
        // Step 1. Generating player accounts
        List<GamePlayerAccount> playerAccounts = new ArrayList<>(players.size());
        long ammount = initiation.getSpecification().getPrice().getAmount();
        for(Long player: players) {
            playerAccounts.add(new GamePlayerAccount(player, ammount));
        }
        GameAccount account = new GameAccount(playerAccounts);
        // Step 2. Processing generated accounts
        GamePlayerIterator playerIterator = new SequentialPlayerIterator(playerAccounts);
        // Step 3. 
        return new PicPacPoeState(account, playerIterator);
    }

}
