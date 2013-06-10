package com.gogomaya.server.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.game.event.client.GiveUpEvent;
import com.gogomaya.server.game.event.client.MoveTimeoutSurrenderEvent;
import com.gogomaya.server.game.event.client.TotalTimeoutSurrenderEvent;
import com.gogomaya.server.player.PlayerAware;
import com.gogomaya.server.tictactoe.event.client.TicTacToeBetOnCellEvent;
import com.gogomaya.server.tictactoe.event.client.TicTacToeSelectCellEvent;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ 
    @Type(value = TicTacToeSelectCellEvent.class, name = "select"),
    @Type(value = TicTacToeBetOnCellEvent.class, name = "bet"),
    @Type(value = GiveUpEvent.class, name = "giveUp"),
    @Type(value = MoveTimeoutSurrenderEvent.class, name = "moveTimeBreached"),
    @Type(value = TotalTimeoutSurrenderEvent.class, name = "totalTimeBreached")})
public interface ClientEvent extends PlayerAware {

}
