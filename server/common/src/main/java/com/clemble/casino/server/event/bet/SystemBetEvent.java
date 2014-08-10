package com.clemble.casino.server.event.bet;

import com.clemble.casino.bet.BetAware;
import com.clemble.casino.event.Event;
import com.clemble.casino.payment.PaymentTransactionAware;
import com.clemble.casino.server.event.SystemEvent;

/**
 * Created by mavarazy on 8/9/14.
 */
public interface SystemBetEvent extends PaymentTransactionAware, SystemEvent {
}
