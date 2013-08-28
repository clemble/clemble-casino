package com.gogomaya.server.game.iterator;

import com.gogomaya.server.game.construct.GameInitiation;

public class GamePlayerIteratorFactory {

    public static GamePlayerIterator create(GameInitiation initiation) {
        return new SequentialPlayerIterator(initiation.getParticipants());
    }

}
