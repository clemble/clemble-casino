package com.clemble.casino.game.specification;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.rule.bet.BetRule;
import com.clemble.casino.game.rule.bet.FixedBetRule;
import com.clemble.casino.game.rule.construct.PlayerNumberRule;
import com.clemble.casino.game.rule.construct.PrivacyRule;
import com.clemble.casino.game.rule.giveup.GiveUpRule;
import com.clemble.casino.game.rule.time.MoveTimeRule;
import com.clemble.casino.game.rule.time.TotalTimeRule;
import com.clemble.casino.game.rule.visibility.VisibilityRule;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;

@Entity
@Table(name = "GAME_SPECIFICATION")
public class GameSpecification implements Serializable {

    /**
     * Generated
     */
    private static final long serialVersionUID = 6573909004152898162L;

    final public static GameSpecification DEFAULT = new GameSpecification().setName(new GameSpecificationKey(Game.pic, "DEFAULT"))
            .setBetRule(FixedBetRule.DEFAULT).setPrice(Money.create(Currency.FakeMoney, 50)).setGiveUpRule(GiveUpRule.lost)
            .setMoveTimeRule(MoveTimeRule.DEFAULT).setTotalTimeRule(TotalTimeRule.DEFAULT).setNumberRule(PlayerNumberRule.two)
            .setPrivacayRule(PrivacyRule.everybody);

    @EmbeddedId
    private GameSpecificationKey name;

    @Type(type = "com.clemble.casino.payment.money.MoneyHibernate")
    @Columns(columns = { @Column(name = "CURRENCY"), @Column(name = "PRICE") })
    private Money price;

    @Type(type = "com.clemble.casino.game.rule.bet.BetRuleHibernate")
    @Columns(columns = { @Column(name = "BET_RULE") })
    private BetRule betRule;

    @Column(name = "GIVE_UP")
    @Enumerated(EnumType.STRING)
    private GiveUpRule giveUpRule;

    @Type(type = "com.clemble.casino.game.rule.time.MoveTimeRuleHibernate")
    @Columns(columns = { @Column(name = "MOVE_TIME_BREACH"), @Column(name = "MOVE_TIME_LIMIT"), })
    private MoveTimeRule moveTimeRule;

    @Type(type = "com.clemble.casino.game.rule.time.TotalTimeRule")
    @Columns(columns = { @Column(name = "TOTAL_TIME_BREACH"), @Column(name = "TOTAL_TIME_LIMIT"), })
    private TotalTimeRule totalTimeRule;

    @Column(name = "PRIVACY_RULE")
    @Enumerated(EnumType.STRING)
    private PrivacyRule privacyRule;

    @Column(name = "PLAYER_NUMBER")
    @Enumerated(EnumType.STRING)
    private PlayerNumberRule numberRule;

    @Column(name = "VISIBILITY")
    @Enumerated(EnumType.STRING)
    private VisibilityRule visibilityRule;

    public GameSpecification() {
    }

    public GameSpecificationKey getName() {
        return name;
    }

    public GameSpecification setName(GameSpecificationKey name) {
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

    public Money getPrice() {
        return price;
    }

    public GameSpecification setPrice(Money price) {
        this.price = price;
        return this;
    }

    public BetRule getBetRule() {
        return betRule;
    }

    public GameSpecification setBetRule(BetRule betRule) {
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

    public VisibilityRule getVisibilityRule() {
        return visibilityRule;
    }

    public void setVisibilityRule(VisibilityRule visibilityRule) {
        this.visibilityRule = visibilityRule;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((betRule == null) ? 0 : betRule.hashCode());
        result = prime * result + ((giveUpRule == null) ? 0 : giveUpRule.hashCode());
        result = prime * result + ((moveTimeRule == null) ? 0 : moveTimeRule.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((numberRule == null) ? 0 : numberRule.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((privacyRule == null) ? 0 : privacyRule.hashCode());
        result = prime * result + ((totalTimeRule == null) ? 0 : totalTimeRule.hashCode());
        result = prime * result + ((visibilityRule == null) ? 0 : visibilityRule.hashCode());
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
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        if (privacyRule != other.privacyRule)
            return false;
        if (totalTimeRule == null) {
            if (other.totalTimeRule != null)
                return false;
        } else if (!totalTimeRule.equals(other.totalTimeRule))
            return false;
        if (visibilityRule != other.visibilityRule)
            return false;
        return true;
    }

}
