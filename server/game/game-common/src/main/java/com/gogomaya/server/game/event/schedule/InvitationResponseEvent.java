package com.gogomaya.server.game.event.schedule;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.event.GameConstructionEvent;
import com.gogomaya.server.player.PlayerAware;

public interface InvitationResponseEvent extends GameConstructionEvent, PlayerAware, ClientEvent {

}
