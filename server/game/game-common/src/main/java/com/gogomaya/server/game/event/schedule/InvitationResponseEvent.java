package com.gogomaya.server.game.event.schedule;

import com.gogomaya.player.PlayerAware;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.event.GameConstructionEvent;

public interface InvitationResponseEvent extends GameConstructionEvent, PlayerAware, ClientEvent {

}
