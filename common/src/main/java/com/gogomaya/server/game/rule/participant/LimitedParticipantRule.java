package com.gogomaya.server.game.rule.participant;

// Can additionally define player level
public class LimitedParticipantRule extends ParticipantRule {

    final private int minParticipants;

    final private int maxParticipants;

    private LimitedParticipantRule(final ParticipantMatchType matchType, final ParticipantPrivacyType privacyType, final int minParticipants, final int maxParticipants) {
        super(ParticipantType.Limited, matchType, privacyType);
        this.maxParticipants = maxParticipants;
        this.minParticipants = minParticipants;
    }

    public int getMinPlayers() {
        return minParticipants;
    }

    public int getMaxPlayers() {
        return maxParticipants;
    }

    public static LimitedParticipantRule create(final ParticipantMatchType matchType, final ParticipantPrivacyType privacyType, final int minParticipants, final int maxParticipants) {
        return new LimitedParticipantRule(matchType, privacyType, minParticipants, maxParticipants);
    }

}