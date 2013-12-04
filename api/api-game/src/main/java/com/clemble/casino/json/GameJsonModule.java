package com.clemble.casino.json;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;

import com.clemble.casino.game.account.InvisibleGameAccount;
import com.clemble.casino.game.account.VisibleGameAccount;
import com.clemble.casino.game.configuration.SelectRuleOptions;
import com.clemble.casino.game.configuration.SelectSpecificationOptions;
import com.clemble.casino.game.construct.AutomaticGameRequest;
import com.clemble.casino.game.construct.AvailabilityGameRequest;
import com.clemble.casino.game.construct.ManagerGameConstructionRequest;
import com.clemble.casino.game.construct.ScheduledGameRequest;
import com.clemble.casino.game.event.client.BetEvent;
import com.clemble.casino.game.event.client.management.LeaveTableEvent;
import com.clemble.casino.game.event.client.surrender.GiveUpEvent;
import com.clemble.casino.game.event.client.surrender.MoveTimeoutSurrenderEvent;
import com.clemble.casino.game.event.client.surrender.TotalTimeoutSurrenderEvent;
import com.clemble.casino.game.event.schedule.GameCanceledEvent;
import com.clemble.casino.game.event.schedule.GameConstructedEvent;
import com.clemble.casino.game.event.schedule.InvitationAcceptedEvent;
import com.clemble.casino.game.event.schedule.InvitationDeclinedEvent;
import com.clemble.casino.game.event.schedule.PlayerInvitedEvent;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.event.server.GameStartedEvent;
import com.clemble.casino.game.event.server.PlayerMovedEvent;
import com.clemble.casino.game.event.server.PlayerSurrenderedEvent;
import com.clemble.casino.game.iterator.SequentialPlayerIterator;
import com.clemble.casino.game.outcome.DrawOutcome;
import com.clemble.casino.game.outcome.NoOutcome;
import com.clemble.casino.game.outcome.PlayerWonOutcome;
import com.clemble.casino.game.rule.bet.BetRule;
import com.clemble.casino.game.rule.bet.FixedBetRule;
import com.clemble.casino.game.rule.bet.LimitedBetRule;
import com.clemble.casino.game.rule.bet.UnlimitedBetRule;
import com.clemble.casino.game.rule.construct.PlayerNumberRule;
import com.clemble.casino.game.rule.construct.PrivacyRule;
import com.clemble.casino.game.rule.giveup.GiveUpRule;
import com.clemble.casino.game.rule.time.MoveTimeRule;
import com.clemble.casino.game.rule.time.TotalTimeRule;

class GameJsonModule implements ClembleJsonModule {

    @Override
    public Module construct() {
        SimpleModule module = new SimpleModule("Game");
        module.registerSubtypes(new NamedType(InvisibleGameAccount.class, InvisibleGameAccount.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(VisibleGameAccount.class, VisibleGameAccount.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(SelectRuleOptions.class, SelectRuleOptions.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(SelectSpecificationOptions.class, SelectSpecificationOptions.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(AutomaticGameRequest.class, AutomaticGameRequest.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(AvailabilityGameRequest.class, AvailabilityGameRequest.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(ManagerGameConstructionRequest.class, ManagerGameConstructionRequest.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(ScheduledGameRequest.class, ScheduledGameRequest.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(BetEvent.class, BetEvent.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(LeaveTableEvent.class, LeaveTableEvent.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(GiveUpEvent.class, GiveUpEvent.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(MoveTimeoutSurrenderEvent.class, MoveTimeoutSurrenderEvent.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(TotalTimeoutSurrenderEvent.class, TotalTimeoutSurrenderEvent.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(GameCanceledEvent.class, GameCanceledEvent.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(GameConstructedEvent.class, GameConstructedEvent.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(InvitationAcceptedEvent.class, InvitationAcceptedEvent.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(InvitationDeclinedEvent.class, InvitationDeclinedEvent.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(PlayerInvitedEvent.class, PlayerInvitedEvent.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(GameEndedEvent.class, GameEndedEvent.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(GameStartedEvent.class, GameStartedEvent.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(PlayerMovedEvent.class, PlayerMovedEvent.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(PlayerSurrenderedEvent.class, PlayerSurrenderedEvent.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(SequentialPlayerIterator.class, SequentialPlayerIterator.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(DrawOutcome.class, DrawOutcome.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(NoOutcome.class, NoOutcome.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(PlayerWonOutcome.class, PlayerWonOutcome.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(BetRule.class, BetRule.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(FixedBetRule.class, FixedBetRule.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(LimitedBetRule.class, LimitedBetRule.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(UnlimitedBetRule.class, UnlimitedBetRule.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(PlayerNumberRule.class, PlayerNumberRule.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(PrivacyRule.class, PrivacyRule.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(GiveUpRule.class, GiveUpRule.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(MoveTimeRule.class, MoveTimeRule.class.getAnnotation(JsonTypeName.class).value()));
        module.registerSubtypes(new NamedType(TotalTimeRule.class, TotalTimeRule.class.getAnnotation(JsonTypeName.class).value()));
        return module;
    }

}
