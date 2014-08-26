package com.clemble.casino.server.event.bet;

import com.clemble.casino.payment.PaymentTransactionAware;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.event.SystemEvent;

/**
 * Created by mavarazy on 8/9/14.
 */
public interface SystemBetEvent extends PaymentTransactionAware, SystemEvent, PlayerAware {
}
