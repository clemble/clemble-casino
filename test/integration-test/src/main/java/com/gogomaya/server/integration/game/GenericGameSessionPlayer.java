package com.gogomaya.server.integration.game;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.event.client.GameClientEvent;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.player.Player;

public class GenericGameSessionPlayer<State extends GameState> implements GameSessionPlayer<State> {

    /**
     * Generated 05/07/13
     */
    private static final long serialVersionUID = -4604087499745502553L;

    final protected GameSessionPlayer<State> actualPlayer;

    public GenericGameSessionPlayer(GameSessionPlayer<State> delegate) {
        this.actualPlayer = checkNotNull(delegate);
    }

    @Override
    public Player getPlayer() {
        return actualPlayer.getPlayer();
    }

    @Override
    public long getPlayerId() {
        return actualPlayer.getPlayerId();
    }

    @Override
    final public long getConstruction() {
        return actualPlayer.getConstruction();
    }

    @Override
    public GameConstruction getConstructionInfo() {
        return actualPlayer.getConstructionInfo();
    }

    @Override
    final public GameSpecification getSpecification() {
        return actualPlayer.getSpecification();
    }

    @Override
    final public State getState() {
        return actualPlayer.getState();
    }

    @Override
    final public boolean isAlive() {
        return actualPlayer.isAlive();
    }

    @Override
    final public void syncWith(GameSessionPlayer<State> anotherState) {
        actualPlayer.syncWith(anotherState);
    }

    @Override
    final public void waitForStart() {
        actualPlayer.waitForStart();
    }

    @Override
    final public void waitForTurn() {
        actualPlayer.waitForTurn();
    }

    @Override
    final public boolean isToMove() {
        return actualPlayer.isToMove();
    }

    @Override
    final public ClientEvent getNextMove() {
        return actualPlayer.getNextMove();
    }

    @Override
    final public void perform(GameClientEvent gameAction) {
        actualPlayer.perform(gameAction);
    }

    @Override
    final public void giveUp() {
        actualPlayer.giveUp();
    }

    @Override
    final public void close() {
        actualPlayer.close();
    }

    @Override
    public int getVersion() {
        return actualPlayer.getVersion();
    }

    @Override
    public void waitVersion(int version) {
        actualPlayer.waitVersion(version);
    }
}
