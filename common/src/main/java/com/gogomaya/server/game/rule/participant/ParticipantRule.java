package com.gogomaya.server.game.rule.participant;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.GameRule;

abstract public class ParticipantRule implements GameRule {

    final private ParticipantMatchType matchType;

    final private ParticipantPrivacyType privacyType;

    private ParticipantRule(final ParticipantMatchType matchType, final ParticipantPrivacyType privacyType) {
        this.matchType = checkNotNull(matchType);
        this.privacyType = checkNotNull(privacyType);
    }

    public ParticipantMatchType getMatchType() {
        return matchType;
    }

    public ParticipantPrivacyType getPrivacyType() {
        return privacyType;
    }

    // Can additionally define player level
    public static class LimitedParticipantRule extends ParticipantRule {

        final private int minParticipants;

        final private int maxParticipants;

        private LimitedParticipantRule(final ParticipantMatchType matchType, final ParticipantPrivacyType privacyType, final int minParticipants, final int maxParticipants) {
            super(matchType, privacyType);
            this.maxParticipants = maxParticipants;
            this.minParticipants = minParticipants;
        }

        public int getMinPlayers() {
            return minParticipants;
        }

        public int getMaxPlayers() {
            return maxParticipants;
        }

    }
}
