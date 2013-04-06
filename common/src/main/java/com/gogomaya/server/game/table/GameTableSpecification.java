package com.gogomaya.server.game.table;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.game.table.GameTableSpecificationFormats.GameTableSpecificationJsonDeserializer;
import com.gogomaya.server.game.table.GameTableSpecificationFormats.GameTableSpecificationJsonSerializer;
import com.gogomaya.server.game.table.rule.GameTableMatchRule;
import com.gogomaya.server.game.table.rule.GameTablePlayerNumberRule;
import com.gogomaya.server.game.table.rule.GameTablePrivacyRule;

@JsonSerialize(using = GameTableSpecificationJsonSerializer.class)
@JsonDeserialize(using = GameTableSpecificationJsonDeserializer.class)
@Embeddable
public class GameTableSpecification implements GameTableRule {

    final public static GameTableMatchRule DEFAULT_MATCH_RULE = GameTableMatchRule.automatic;
    final public static GameTablePrivacyRule DEFAULT_PRIVACY_RULE = GameTablePrivacyRule.Public;
    final public static GameTablePlayerNumberRule DEFAULT_NUMBER_RULE = GameTablePlayerNumberRule.create(2, 2);

    final public static GameTableSpecification DEFAULT_TABLE_SPECIFICATION = GameTableSpecification.create(DEFAULT_MATCH_RULE, DEFAULT_PRIVACY_RULE,
            DEFAULT_NUMBER_RULE);

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = -6212991143939664300L;

    @Embedded
    private GameTablePlayerNumberRule numberRule;

    @Column(name = "TABLE_MATCH_RULE")
    private GameTableMatchRule matchRule;

    @Column(name = "TABLE_PRIVACY_RULE")
    private GameTablePrivacyRule privacyType;

    public GameTableSpecification() {
    }

    private GameTableSpecification(final GameTableMatchRule matchType, final GameTablePrivacyRule privacyType, final GameTablePlayerNumberRule playerNumberRule) {
        this.numberRule = checkNotNull(playerNumberRule);
        this.matchRule = checkNotNull(matchType);
        this.privacyType = checkNotNull(privacyType);
    }

    public GameTableMatchRule getMatchRule() {
        return matchRule;
    }

    public GameTableSpecification setMatchRule(GameTableMatchRule newMatchRule) {
        this.matchRule = newMatchRule;
        return this;
    }

    public GameTablePrivacyRule getPrivacyRule() {
        return privacyType;
    }

    public GameTableSpecification setPrivacyRule(GameTablePrivacyRule newPrivacyRule) {
        this.privacyType = newPrivacyRule;
        return this;
    }

    public GameTablePlayerNumberRule getNumberRule() {
        return numberRule;
    }

    public GameTableSpecification setNumberRule(GameTablePlayerNumberRule newNumberRule) {
        this.numberRule = newNumberRule;
        return this;
    }

    @JsonCreator
    public static GameTableSpecification create(
            @JsonProperty("matchRule") final GameTableMatchRule matchRule,
            @JsonProperty("privacyRule") final GameTablePrivacyRule playerPrivacyRule,
            @JsonProperty("numberRule") final GameTablePlayerNumberRule playerNumberRule) {
        return new GameTableSpecification(matchRule, playerPrivacyRule, playerNumberRule);
    }

}
