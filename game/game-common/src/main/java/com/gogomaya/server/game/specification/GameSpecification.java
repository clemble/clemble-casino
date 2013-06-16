package com.gogomaya.server.game.specification;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.rule.construction.PlayerNumberRule;
import com.gogomaya.server.game.rule.construction.PrivacyRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TimeRuleFormat.MoveTimeRuleHibernateType;
import com.gogomaya.server.game.rule.time.TimeRuleFormat.TotalTimeRuleHibernateType;
import com.gogomaya.server.game.rule.time.TotalTimeRule;
import com.gogomaya.server.money.Currency;

@Entity
@Table(name = "GAME_SPECIFICATION")
@TypeDefs(value = {
        @TypeDef(name = "totalTime", typeClass = TotalTimeRuleHibernateType.class),
        @TypeDef(name = "moveTime", typeClass = MoveTimeRuleHibernateType.class)
})
public class GameSpecification implements Serializable {

    /**
     * Generated 
     */
    private static final long serialVersionUID = 6573909004152898162L;
    
    final public static GameSpecification DEFAULT = new GameSpecification()
        .setName(new SpecificationName("DEFAULT", ""))
        .setBetRule(new FixedBetRule(50))
        .setCurrency(Currency.FakeMoney)
        .setGiveUpRule(GiveUpRule.lost)
        .setMoveTimeRule(MoveTimeRule.DEFAULT)
        .setTotalTimeRule(TotalTimeRule.DEFAULT)
        .setNumberRule(PlayerNumberRule.two)
        .setPrivacayRule(PrivacyRule.everybody);

    @EmbeddedId
    private SpecificationName name;

    @Column(name = "CURRENCY")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Embedded
    private FixedBetRule betRule;

    @Column(name = "GIVE_UP")
    @Enumerated(EnumType.STRING)
    private GiveUpRule giveUpRule;

    @Type(type = "moveTime")
    @Columns(columns = { @Column(name = "MOVE_TIME_BREACH"), @Column(name = "MOVE_TIME_LIMIT"), })
    private MoveTimeRule moveTimeRule;

    @Type(type = "totalTime")
    @Columns(columns = { @Column(name = "TOTAL_TIME_BREACH"), @Column(name = "TOTAL_TIME_LIMIT"), })
    private TotalTimeRule totalTimeRule;

    @Column(name = "PRIVACY_RULE")
    @Enumerated(EnumType.STRING)
    private PrivacyRule privacyRule;

    @Column(name = "PLAYER_NUMBER")
    @Enumerated(EnumType.STRING)
    private PlayerNumberRule numberRule;

    public GameSpecification() {
    }

    public SpecificationName getName() {
        return name;
    }

    public GameSpecification setName(SpecificationName name) {
        this.name = name;
        return this;
    }

    public PrivacyRule getPrivacyRule() {
        return privacyRule;
    }

    public GameSpecification setPrivacayRule(PrivacyRule privacyRule) {
        this.privacyRule = privacyRule;
        return this;
    }

    public PlayerNumberRule getNumberRule() {
        return numberRule;
    }

    public GameSpecification setNumberRule(PlayerNumberRule numberRule) {
        this.numberRule = numberRule;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public GameSpecification setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public FixedBetRule getBetRule() {
        return betRule;
    }

    public GameSpecification setBetRule(FixedBetRule betRule) {
        this.betRule = betRule;
        return this;
    }

    public GiveUpRule getGiveUpRule() {
        return giveUpRule;
    }

    public GameSpecification setGiveUpRule(GiveUpRule giveUpRule) {
        this.giveUpRule = giveUpRule;
        return this;
    }

    public MoveTimeRule getMoveTimeRule() {
        return moveTimeRule;
    }

    public GameSpecification setMoveTimeRule(MoveTimeRule moveTimeRule) {
        this.moveTimeRule = moveTimeRule;
        return this;
    }

    public TotalTimeRule getTotalTimeRule() {
        return totalTimeRule;
    }

    public GameSpecification setTotalTimeRule(TotalTimeRule totalTimeRule) {
        this.totalTimeRule = totalTimeRule;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((betRule == null) ? 0 : betRule.hashCode());
        result = prime * result + ((currency == null) ? 0 : currency.hashCode());
        result = prime * result + ((giveUpRule == null) ? 0 : giveUpRule.hashCode());
        result = prime * result + ((moveTimeRule == null) ? 0 : moveTimeRule.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((numberRule == null) ? 0 : numberRule.hashCode());
        result = prime * result + ((privacyRule == null) ? 0 : privacyRule.hashCode());
        result = prime * result + ((totalTimeRule == null) ? 0 : totalTimeRule.hashCode());
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
        GameSpecification other = (GameSpecification) obj;
        if (betRule == null) {
            if (other.betRule != null)
                return false;
        } else if (!betRule.equals(other.betRule))
            return false;
        if (currency != other.currency)
            return false;
        if (giveUpRule != other.giveUpRule)
            return false;
        if (moveTimeRule == null) {
            if (other.moveTimeRule != null)
                return false;
        } else if (!moveTimeRule.equals(other.moveTimeRule))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (numberRule != other.numberRule)
            return false;
        if (privacyRule != other.privacyRule)
            return false;
        if (totalTimeRule == null) {
            if (other.totalTimeRule != null)
                return false;
        } else if (!totalTimeRule.equals(other.totalTimeRule))
            return false;
        return true;
    }

}
