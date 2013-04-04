package com.gogomaya.server.game.table.rule;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.game.table.GameTableRule;
import com.gogomaya.server.game.table.rule.GameTableSpecificationFormats.GameTableSpecificationJsonDeserializer;
import com.gogomaya.server.game.table.rule.GameTableSpecificationFormats.GameTableSpecificationJsonSerializer;

@JsonSerialize(using = GameTableSpecificationJsonSerializer.class)
@JsonDeserialize(using = GameTableSpecificationJsonDeserializer.class)
@Embeddable
public class GameTableSpecification implements GameTableRule {

    final public static PlayerMatchRule DEFAULT_MATCH_RULE = PlayerMatchRule.Automatic;
    final public static PlayerPrivacyRule DEFAULT_PRIVACY_RULE = PlayerPrivacyRule.Public;
    final public static PlayerNumberRule DEFAULT_NUMBER_RULE = PlayerNumberRule.create(2, 2);

    final public static GameTableSpecification DEFAULT_TABLE_SPECIFICATION = GameTableSpecification.create(DEFAULT_MATCH_RULE, DEFAULT_PRIVACY_RULE,
            DEFAULT_NUMBER_RULE);

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = -6212991143939664300L;

    @Embedded
    private PlayerNumberRule numberRule;

    @Column(name = "TABLE_MATCH_RULE")
    private PlayerMatchRule matchRule;

    @Column(name = "TABLE_PRIVACY_RULE")
    private PlayerPrivacyRule privacyType;

    public GameTableSpecification() {
    }

    private GameTableSpecification(final PlayerMatchRule matchType, final PlayerPrivacyRule privacyType, final PlayerNumberRule playerNumberRule) {
        this.numberRule = checkNotNull(playerNumberRule);
        this.matchRule = checkNotNull(matchType);
        this.privacyType = checkNotNull(privacyType);
    }

    public PlayerMatchRule getMatchRule() {
        return matchRule;
    }

    public GameTableSpecification setMatchRule(PlayerMatchRule newMatchRule) {
        this.matchRule = newMatchRule;
        return this;
    }

    public PlayerPrivacyRule getPrivacyRule() {
        return privacyType;
    }

    public GameTableSpecification setPrivacyRule(PlayerPrivacyRule newPrivacyRule) {
        this.privacyType = newPrivacyRule;
        return this;
    }

    public PlayerNumberRule getNumberRule() {
        return numberRule;
    }

    public GameTableSpecification setNumberRule(PlayerNumberRule newNumberRule) {
        this.numberRule = newNumberRule;
        return this;
    }

    @JsonCreator
    public static GameTableSpecification create(
            @JsonProperty("matchRule") final PlayerMatchRule matchRule,
            @JsonProperty("privacyRule") final PlayerPrivacyRule playerPrivacyRule,
            @JsonProperty("numberRule") final PlayerNumberRule playerNumberRule) {
        return new GameTableSpecification(matchRule, playerPrivacyRule, playerNumberRule);
    }

}
