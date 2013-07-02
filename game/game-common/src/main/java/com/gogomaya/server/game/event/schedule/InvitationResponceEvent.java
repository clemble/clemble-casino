package com.gogomaya.server.game.event.schedule;

import com.gogomaya.server.event.ExpectedAction;
import com.gogomaya.server.event.GameConstructionEvent;
import com.gogomaya.server.player.PlayerAware;

public interface InvitationResponceEvent extends GameConstructionEvent, PlayerAware, ExpectedAction {

}
