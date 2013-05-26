package com.gogomaya.server.integration.emulator;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.integration.game.GamePlayer;

public class GameActor<State extends GameState> {

    final GamePlayer<State> player;

    public GameActor(final GamePlayer<State> player) {
        this.player = player;
    }

    public void act() {
        // Step 1. Waiting for the next player move
        while (!player.isToMove()) {
            player.waitForUpdate();
        }
        // Step 2. Performing actual move
        move();
    }

    public void move() {
    }
}
