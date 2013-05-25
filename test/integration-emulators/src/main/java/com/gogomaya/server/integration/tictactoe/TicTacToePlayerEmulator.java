package com.gogomaya.server.integration.tictactoe;

import static com.google.common.base.Preconditions.checkNotNull;

public class TicTacToePlayerEmulator {

    final TicTacToePlayer player;

    public TicTacToePlayerEmulator(TicTacToePlayer player) {
        this.player = checkNotNull(player);
    }

    public void emulate() {
        if (!player.isToMove()) {
            player.waitForUpdate();
        } else {
            
        }
    }

}
