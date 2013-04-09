package com.gogomaya.server.game.table;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
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
public class GameTableSpecification implements Serializable {

    final public static GameTableSpecification DEFAULT = GameTableSpecification.create(GameTableMatchRule.DEFAULT, GameTablePrivacyRule.DEFAULT,
            GameTablePlayerNumberRule.DEFAULT);

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

    @JsonIgnore
    public GameTableSpecification() {
    }

    @JsonIgnore
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
            @JsonProperty("match") final GameTableMatchRule matchRule,
            @JsonProperty("privacy") final GameTablePrivacyRule playerPrivacyRule,
            @JsonProperty("number") final GameTablePlayerNumberRule playerNumberRule) {
        return new GameTableSpecification(matchRule, playerPrivacyRule, playerNumberRule);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((matchRule == null) ? 0 : matchRule.hashCode());
        result = prime * result + ((numberRule == null) ? 0 : numberRule.hashCode());
        result = prime * result + ((privacyType == null) ? 0 : privacyType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameTableSpecification other = (GameTableSpecification) obj;
        if (matchRule != other.matchRule)
            return false;
        if (numberRule == null) {
            if (other.numberRule != null)
                return false;
        } else if (!numberRule.equals(other.numberRule))
            return false;
        if (privacyType != other.privacyType)
            return false;
        return true;
    }

}
