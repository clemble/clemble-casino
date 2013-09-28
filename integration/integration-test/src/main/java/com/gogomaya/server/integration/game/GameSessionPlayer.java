package com.gogomaya.server.integration.game;

import java.io.Closeable;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameState;
import com.gogomaya.game.SessionAware;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.event.client.GameClientEvent;
import com.gogomaya.game.specification.GameSpecificationAware;
import com.gogomaya.server.integration.player.Player;

public interface GameSessionPlayer<State extends GameState> extends SessionAware, Closeable, GameSpecificationAware {

    public Player getPlayer();

    public GameConstruction getConstructionInfo();

    public State getState();

    public boolean isAlive();

    public void syncWith(GameSessionPlayer<State> anotherState);

    public void waitForStart();

    public void waitForTurn();

    public int getVersion();

    public void waitVersion(int version);

    public boolean isToMove();

    public ClientEvent getNextMove();

    public void perform(GameClientEvent gameAction);

    public void giveUp();

    public void close();

}
