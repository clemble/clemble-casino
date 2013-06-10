package com.gogomaya.server.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.game.event.client.GiveUpEvent;
import com.gogomaya.server.player.PlayerAware;
import com.gogomaya.server.tictactoe.event.client.TicTacToeBetOnCellEvent;
import com.gogomaya.server.tictactoe.event.client.TicTacToeSelectCellEvent;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ 
    @Type(value = TicTacToeSelectCellEvent.class, name = "select"),
    @Type(value = TicTacToeBetOnCellEvent.class, name = "bet"),
    @Type(value = GiveUpEvent.class, name = "giveUp")})
public interface ClientEvent extends PlayerAware {

}
