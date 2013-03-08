package com.gogomaya.server.game.rule.participant;

final public class FixedParticipantRule extends ParticipantRule {

    final private int numberOfParticipants;

    private FixedParticipantRule(final ParticipantMatchType matchType, final ParticipantPrivacyType privacyType, final int numberOfParticipants) {
        super(ParticipantType.Fixed, matchType, privacyType);
        this.numberOfParticipants = numberOfParticipants;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public static FixedParticipantRule create(final ParticipantMatchType matchType, final ParticipantPrivacyType privacyType, final int numberOfParticipants) {
        return new FixedParticipantRule(matchType, privacyType, numberOfParticipants);
    }

}
