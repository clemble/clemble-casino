package com.clemble.casino.integration.emulator;

import com.clemble.casino.game.GameAware;
import com.clemble.casino.game.GameState;
import com.clemble.casino.integration.game.GamePlayer;
import com.clemble.casino.integration.game.MatchGamePlayer;

abstract public class MatchGamePlayerActor<State extends GameState> implements GamePlayerActor<MatchGamePlayer<State>> {

    @Override
    public void play(MatchGamePlayer<State> matchPlayer) {
        // Step 1. Sanity check
        matchPlayer.waitForStart(0);
        while (matchPlayer.isAlive()) {
            // Step 2. Waiting for player turn
            matchPlayer.waitForTurn();
            // Step 3. Performing action
            if (matchPlayer.isToMove()) {
                int versionBefore = matchPlayer.getVersion();
                doMove(matchPlayer);
                matchPlayer.waitVersion(versionBefore + 1);
            }
        }
    }

    abstract public void doMove(MatchGamePlayer player);

}
