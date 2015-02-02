package com.clemble.casino.integration.game.construction;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.client.event.PlayerEventSelector;
import com.clemble.casino.event.Event;
import com.clemble.casino.integration.event.EventAccumulator;
import com.clemble.casino.server.event.EventByTemplateSelector;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.event.email.SystemEmailSendDirectRequestEvent;
import com.clemble.casino.server.event.email.SystemEmailSendRequestEvent;

/**
 * Created by mavarazy on 2/2/15.
 */
public class EmailScenarios {

    final private EventAccumulator eventAccumulator;

    public EmailScenarios(EventAccumulator<SystemEvent> eventAccumulator) {
        this.eventAccumulator = eventAccumulator;
    }

    public void verify(ClembleCasinoOperations A) {
        // Step 1. Fetching event send request
        SystemEmailSendDirectRequestEvent sendRequestEvent = (SystemEmailSendDirectRequestEvent) eventAccumulator.
            waitFor(EventSelectors.
                    where(new PlayerEventSelector(A.getPlayer())).
                    and(new EventTypeSelector(SystemEmailSendDirectRequestEvent.class))
            );
        // Step 2. Extracting verification code
        String url = sendRequestEvent.getParams().get("url");
        String verificationCode = url.substring(url.lastIndexOf("=") + 1);
        A.emailService().verify(verificationCode);
    }

}
