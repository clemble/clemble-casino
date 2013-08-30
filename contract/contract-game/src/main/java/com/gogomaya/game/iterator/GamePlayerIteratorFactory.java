package com.gogomaya.game.iterator;

import com.gogomaya.game.construct.GameInitiation;

public class GamePlayerIteratorFactory {

    public static GamePlayerIterator create(GameInitiation initiation) {
        return new SequentialPlayerIterator(initiation.getParticipants());
    }

}
