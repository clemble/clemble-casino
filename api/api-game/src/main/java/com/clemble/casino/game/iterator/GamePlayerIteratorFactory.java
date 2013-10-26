package com.clemble.casino.game.iterator;

import com.clemble.casino.game.construct.GameInitiation;

public class GamePlayerIteratorFactory {

    public static GamePlayerIterator create(GameInitiation initiation) {
        return new SequentialPlayerIterator(initiation.getParticipants());
    }

}
