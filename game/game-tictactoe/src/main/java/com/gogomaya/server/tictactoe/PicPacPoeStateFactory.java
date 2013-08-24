package com.gogomaya.server.tictactoe;

import com.gogomaya.server.game.account.GameAccount;
import com.gogomaya.server.game.account.GameAccountFactory;
import com.gogomaya.server.game.action.GameProcessorFactory;
import com.gogomaya.server.game.action.impl.AbstractGameStateFactory;
import com.gogomaya.server.game.construct.GameInitiation;
import com.gogomaya.server.game.iterator.GamePlayerIterator;
import com.gogomaya.server.game.iterator.GamePlayerIteratorFactory;
import com.gogomaya.server.repository.game.GameConstructionRepository;

public class PicPacPoeStateFactory extends AbstractGameStateFactory<PicPacPoeState>{

    public PicPacPoeStateFactory(GameConstructionRepository constructionRepository, GameProcessorFactory<PicPacPoeState> processorFactory) {
        super(constructionRepository, processorFactory);
    }

    @Override
    public PicPacPoeState constructState(GameInitiation initiation) {
        // Step 1. Generating player accounts
        GameAccount account = GameAccountFactory.create(initiation);
        // Step 2. Generating player iterator
        GamePlayerIterator playerIterator = GamePlayerIteratorFactory.create(initiation);
        // Step 3. Generating state
        return new PicPacPoeState(account, playerIterator);
    }

}
