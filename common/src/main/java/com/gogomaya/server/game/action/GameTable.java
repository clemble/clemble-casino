package com.gogomaya.server.game.action;

import java.io.Serializable;
import java.util.Set;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.connection.GameServerConnection;

public interface GameTable<S extends GameSession<?>> extends Serializable {

    public long getTableId();

    public GameServerConnection getServerResource();

    public Set<Long> getPlayers();
    
    public void addPlayer(long player);

    public S getCurrentSession();

    public GameSpecification getSpecification();

}
