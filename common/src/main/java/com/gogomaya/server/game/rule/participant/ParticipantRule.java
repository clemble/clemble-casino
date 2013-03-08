package com.gogomaya.server.game.rule.participant;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.GameRule;

abstract public class ParticipantRule implements GameRule {

    final private ParticipantMatchType matchType;

    final private ParticipantPrivacyType privacyType;

    final private ParticipantType participantType;

    protected ParticipantRule(final ParticipantType participantType, final ParticipantMatchType matchType, final ParticipantPrivacyType privacyType) {
        this.participantType = checkNotNull(participantType);
        this.matchType = checkNotNull(matchType);
        this.privacyType = checkNotNull(privacyType);
    }

    public ParticipantMatchType getMatchType() {
        return matchType;
    }

    public ParticipantPrivacyType getPrivacyType() {
        return privacyType;
    }

    public ParticipantType getParticipantType() {
        return participantType;
    }

}
