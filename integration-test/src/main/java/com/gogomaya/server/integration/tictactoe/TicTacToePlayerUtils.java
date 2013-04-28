package com.gogomaya.server.integration.tictactoe;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.move.GameMove;

public class TicTacToePlayerUtils {

    public static void syncVersions(TicTacToePlayer... players) {
        // Step 1. Fetching max available version
        int maxVersion = 0;
        for (TicTacToePlayer player : players) {
            int version = player.getTable() != null && player.getTable().getState() != null ? getVersion(player.getTable().getState()) : 0;
            maxVersion = maxVersion < version ? version : maxVersion;
        }
        // Step 2. Waiting for every player to be in sync with max version
        for (TicTacToePlayer ticTacToePlayer : players) {
            ticTacToePlayer.waitVersion(maxVersion);
        }
    }

    public static int getVersion(GameState state) {
        int maxVersion = 0;
        for(GameMove nextMove: state.getNextMoves()) {
            if(maxVersion < nextMove.getMoveId())
                maxVersion = nextMove.getMoveId();
        }
        return maxVersion;
    }
}
