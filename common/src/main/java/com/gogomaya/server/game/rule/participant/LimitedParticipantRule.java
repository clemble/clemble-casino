package com.gogomaya.server.game.rule.participant;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.game.rule.participant.ParticipantRuleFormat.CustomParticipantRuleDeserializer;
import com.gogomaya.server.game.rule.participant.ParticipantRuleFormat.CustomParticipantRuleSerializer;

@JsonSerialize(using = CustomParticipantRuleSerializer.class)
@JsonDeserialize(using = CustomParticipantRuleDeserializer.class)
final public class LimitedParticipantRule extends ParticipantRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = -1983738788740436316L;

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

    public static LimitedParticipantRule create(
            final ParticipantMatchType matchType,
            final ParticipantPrivacyType privacyType,
            final int minParticipants,
            final int maxParticipants) {
        return new LimitedParticipantRule(matchType, privacyType, minParticipants, maxParticipants);
    }

}