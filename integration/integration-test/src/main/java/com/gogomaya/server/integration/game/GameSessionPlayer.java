package com.gogomaya.server.integration.game;

import java.io.Closeable;

import com.gogomaya.game.SessionAware;
import com.gogomaya.player.PlayerAware;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.event.client.GameClientEvent;
import com.gogomaya.server.game.specification.GameSpecificationAware;
import com.gogomaya.server.integration.player.Player;

public interface GameSessionPlayer<State extends GameState> extends SessionAware, PlayerAware, Closeable, GameSpecificationAware {

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
