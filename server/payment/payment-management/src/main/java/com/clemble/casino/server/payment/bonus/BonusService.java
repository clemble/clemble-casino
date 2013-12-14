package com.clemble.casino.server.payment.bonus;

import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;

public interface BonusService<S extends SystemEvent> extends SystemEventListener<S>{

}
