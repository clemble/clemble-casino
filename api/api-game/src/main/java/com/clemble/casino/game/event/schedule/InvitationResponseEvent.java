package com.clemble.casino.game.event.schedule;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.event.ConstructionEvent;
import com.clemble.casino.player.PlayerAware;

public interface InvitationResponseEvent extends ConstructionEvent, PlayerAware, ClientEvent {

}
