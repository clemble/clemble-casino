package com.clemble.casino.game.iterator;

import java.io.Serializable;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface GamePlayerIterator extends Serializable {

    public String next();

    public String current();

    public Collection<String> getPlayers();

    public Collection<String> whoIsOpponents(String player);

    public boolean contains(String player);

}
