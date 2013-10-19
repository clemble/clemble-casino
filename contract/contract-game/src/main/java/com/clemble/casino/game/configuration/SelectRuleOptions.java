package com.clemble.casino.game.configuration;

import java.util.Collection;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.rule.bet.BetRule;
import com.clemble.casino.game.rule.construct.PlayerNumberRule;
import com.clemble.casino.game.rule.construct.PrivacyRule;
import com.clemble.casino.game.rule.giveup.GiveUpRule;
import com.clemble.casino.game.rule.time.MoveTimeRule;
import com.clemble.casino.game.rule.time.TotalTimeRule;
import com.clemble.casino.game.specification.GameSpecification;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.clemble.casino.payment.money.Money;

@JsonTypeName("selectRule")
public class SelectRuleOptions implements GameSpecificationOptions {

    /**
     * Generated 10/04/13
     */
    private static final long serialVersionUID = -9099690454645343595L;
    
    final private Game game;

    final private Collection<Money> priceOptions;

    final private GameRuleOptions<BetRule> betOptions;

    final private GameRuleOptions<GiveUpRule> giveUpOptions;

    final private GameRuleOptions<PrivacyRule> privacyOptions;

    final private GameRuleOptions<PlayerNumberRule> numberOptions;

    final private GameRuleOptions<MoveTimeRule> moveTimeOptions;

    final private GameRuleOptions<TotalTimeRule> totalTimeOptions;

    @JsonCreator
    public SelectRuleOptions(@JsonProperty("game") final Game game,
            @JsonProperty("priceOptions") final Collection<Money> priceOptions,
            @JsonProperty("betOptions") final GameRuleOptions<BetRule> betOptions,
            @JsonProperty("giveUpOptions") final GameRuleOptions<GiveUpRule> giveUpOptions,
            @JsonProperty("numberOptions") final GameRuleOptions<PlayerNumberRule> numberOptions,
            @JsonProperty("privacyOptions") final GameRuleOptions<PrivacyRule> privacyOptions,
            @JsonProperty("moveTimeOptions") final GameRuleOptions<MoveTimeRule> moveTimeOptions,
            @JsonProperty("totalTimeOptions") final GameRuleOptions<TotalTimeRule> totalTimeOptions) {
        this.game = game;
        this.betOptions = betOptions;
        this.priceOptions = priceOptions;
        this.giveUpOptions = giveUpOptions;
        this.numberOptions = numberOptions;
        this.privacyOptions = privacyOptions;
        this.moveTimeOptions = moveTimeOptions;
        this.totalTimeOptions = totalTimeOptions;
    }

    public Game getGame() {
        return game;
    }

    public Collection<Money> getPriceOptions() {
        return priceOptions;
    }

    public GameRuleOptions<BetRule> getBetOptions() {
        return betOptions;
    }

    public GameRuleOptions<GiveUpRule> getGiveUpOptions() {
        return giveUpOptions;
    }

    public GameRuleOptions<MoveTimeRule> getMoveTimeOptions() {
        return moveTimeOptions;
    }

    public GameRuleOptions<TotalTimeRule> getTotalTimeOptions() {
        return totalTimeOptions;
    }

    public GameRuleOptions<PrivacyRule> getPrivacyOptions() {
        return privacyOptions;
    }

    public GameRuleOptions<PlayerNumberRule> getNumberOptions() {
        return numberOptions;
    }

    @Override
    public boolean valid(GameSpecification specification) {
        return specification != null &&
              betOptions.contains(specification.getBetRule()) &&
              priceOptions.contains(specification.getPrice()) &&
              giveUpOptions.contains(specification.getGiveUpRule()) &&
              moveTimeOptions.contains(specification.getMoveTimeRule()) &&
              totalTimeOptions.contains(specification.getTotalTimeRule()) &&
              numberOptions.contains(specification.getNumberRule()) &&
              privacyOptions.contains(specification.getPrivacyRule());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((betOptions == null) ? 0 : betOptions.hashCode());
        result = prime * result + ((priceOptions == null) ? 0 : priceOptions.hashCode());
        result = prime * result + ((game == null) ? 0 : game.hashCode());
        result = prime * result + ((giveUpOptions == null) ? 0 : giveUpOptions.hashCode());
        result = prime * result + ((moveTimeOptions == null) ? 0 : moveTimeOptions.hashCode());
        result = prime * result + ((numberOptions == null) ? 0 : numberOptions.hashCode());
        result = prime * result + ((privacyOptions == null) ? 0 : privacyOptions.hashCode());
        result = prime * result + ((totalTimeOptions == null) ? 0 : totalTimeOptions.hashCode());
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
        SelectRuleOptions other = (SelectRuleOptions) obj;
        if (betOptions == null) {
            if (other.betOptions != null)
                return false;
        } else if (!betOptions.equals(other.betOptions))
            return false;
        if (priceOptions == null) {
            if (other.priceOptions != null)
                return false;
        } else if (!priceOptions.containsAll(other.priceOptions) || !other.priceOptions.containsAll(priceOptions))
            return false;
        if (game != other.game)
            return false;
        if (giveUpOptions == null) {
            if (other.giveUpOptions != null)
                return false;
        } else if (!giveUpOptions.equals(other.giveUpOptions))
            return false;
        if (moveTimeOptions == null) {
            if (other.moveTimeOptions != null)
                return false;
        } else if (!moveTimeOptions.equals(other.moveTimeOptions))
            return false;
        if (numberOptions == null) {
            if (other.numberOptions != null)
                return false;
        } else if (!numberOptions.equals(other.numberOptions))
            return false;
        if (privacyOptions == null) {
            if (other.privacyOptions != null)
                return false;
        } else if (!privacyOptions.equals(other.privacyOptions))
            return false;
        if (totalTimeOptions == null) {
            if (other.totalTimeOptions != null)
                return false;
        } else if (!totalTimeOptions.equals(other.totalTimeOptions))
            return false;
        return true;
    }

}
