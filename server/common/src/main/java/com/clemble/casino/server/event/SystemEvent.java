package com.clemble.casino.server.event;

import com.clemble.casino.event.Event;
import com.clemble.casino.server.event.bet.SystemBetCanceledEvent;
import com.clemble.casino.server.event.bet.SystemBetCompletedEvent;
import com.clemble.casino.server.event.email.SystemEmailAddedEvent;
import com.clemble.casino.server.event.game.*;
import com.clemble.casino.server.event.goal.*;
import com.clemble.casino.server.event.notification.SystemNotificationAddEvent;
import com.clemble.casino.server.event.payment.SystemPaymentFreezeRequestEvent;
import com.clemble.casino.server.event.payment.SystemPaymentTransactionRequestEvent;
import com.clemble.casino.server.event.player.*;
import com.clemble.casino.server.event.post.SystemPostAddEvent;
import com.clemble.casino.server.event.schedule.SystemAddJobScheduleEvent;
import com.clemble.casino.server.event.schedule.SystemRemoveJobScheduleEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(value = {
    @JsonSubTypes.Type(name = SystemPlayerLeftEvent.CHANNEL, value = SystemPlayerLeftEvent.class),
    @JsonSubTypes.Type(name = SystemPlayerEnteredEvent.CHANNEL, value = SystemPlayerEnteredEvent.class),
    @JsonSubTypes.Type(name = SystemPlayerPresenceChangedEvent.CHANNEL, value = SystemPlayerPresenceChangedEvent.class),
    @JsonSubTypes.Type(name = SystemPlayerSocialAddedEvent.CHANNEL, value = SystemPlayerSocialAddedEvent.class),
    @JsonSubTypes.Type(name = SystemPlayerDiscoveredConnectionEvent.CHANNEL, value = SystemPlayerDiscoveredConnectionEvent.class),
    @JsonSubTypes.Type(name = SystemPlayerConnectionsFetchedEvent.CHANNEL, value = SystemPlayerConnectionsFetchedEvent.class),
    @JsonSubTypes.Type(name = SystemPlayerCreatedEvent.CHANNEL, value = SystemPlayerCreatedEvent.class),
    @JsonSubTypes.Type(name = SystemPlayerProfileRegisteredEvent.CHANNEL, value = SystemPlayerProfileRegisteredEvent.class),
    @JsonSubTypes.Type(name = SystemPlayerSocialGrantRegisteredEvent.CHANNEL, value = SystemPlayerSocialGrantRegisteredEvent.class),
    @JsonSubTypes.Type(name = SystemPlayerSocialRegisteredEvent.CHANNEL, value = SystemPlayerSocialRegisteredEvent.class),
    @JsonSubTypes.Type(name = SystemGameStartedEvent.CHANNEL, value = SystemGameStartedEvent.class),
    @JsonSubTypes.Type(name = SystemGameEndedEvent.CHANNEL, value = SystemGameEndedEvent.class),
    @JsonSubTypes.Type(name = SystemGameReadyEvent.CHANNEL, value = SystemGameReadyEvent.class),
    @JsonSubTypes.Type(name = SystemGameTimeoutEvent.CHANNEL, value = SystemGameTimeoutEvent.class),
    @JsonSubTypes.Type(name = SystemGameInitiationDueEvent.CHANNEL, value = SystemGameInitiationDueEvent.class),
    @JsonSubTypes.Type(name = SystemPaymentTransactionRequestEvent.CHANNEL, value = SystemPaymentTransactionRequestEvent.class),
    @JsonSubTypes.Type(name = SystemPaymentFreezeRequestEvent.CHANNEL, value = SystemPaymentFreezeRequestEvent.class),
    @JsonSubTypes.Type(name = SystemBetCanceledEvent.CHANNEL, value = SystemBetCanceledEvent.class),
    @JsonSubTypes.Type(name = SystemBetCompletedEvent.CHANNEL, value = SystemBetCompletedEvent.class),
    @JsonSubTypes.Type(name = SystemPlayerImageChangedEvent.CHANNEL, value = SystemPlayerImageChangedEvent.class),
    @JsonSubTypes.Type(name = SystemGoalInitiationDueEvent.CHANNEL, value = SystemGoalInitiationDueEvent.class),
    @JsonSubTypes.Type(name = SystemGoalStartedEvent.CHANNEL, value = SystemGoalStartedEvent.class),
    @JsonSubTypes.Type(name = SystemGoalTimeoutEvent.CHANNEL, value = SystemGoalTimeoutEvent.class),
    @JsonSubTypes.Type(name = SystemAddJobScheduleEvent.CHANNEL, value = SystemAddJobScheduleEvent.class),
    @JsonSubTypes.Type(name = SystemRemoveJobScheduleEvent.CHANNEL, value = SystemRemoveJobScheduleEvent.class),
    @JsonSubTypes.Type(name = SystemNotificationAddEvent.CHANNEL, value = SystemNotificationAddEvent.class),
    @JsonSubTypes.Type(name = SystemPostAddEvent.CHANNEL, value = SystemPostAddEvent.class),
    @JsonSubTypes.Type(name = SystemEmailAddedEvent.CHANNEL, value = SystemEmailAddedEvent.class)
})
public interface SystemEvent extends Event {

    // TODO add serialization checks

    @JsonIgnore
    public String getChannel();

}
