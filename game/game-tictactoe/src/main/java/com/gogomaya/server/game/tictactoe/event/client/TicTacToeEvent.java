package com.gogomaya.server.game.tictactoe.event.client;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.event.ClientEvent;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface TicTacToeEvent extends ClientEvent {

}
