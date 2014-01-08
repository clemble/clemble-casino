package com.clemble.casino.server.payment.bonus;

import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;

abstract public interface BonusEventListener<S extends SystemEvent> extends SystemEventListener<S>{

}
