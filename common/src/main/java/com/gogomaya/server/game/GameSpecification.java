package com.gogomaya.server.game;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import com.gogomaya.server.game.rule.bet.BetRule;
import com.gogomaya.server.game.rule.construction.MatchRule;
import com.gogomaya.server.game.rule.construction.PlayerNumberRule;
import com.gogomaya.server.game.rule.construction.PrivacyRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TotalTimeRule;
import com.gogomaya.server.game.tictactoe.TicTacToeSpecification;
import com.gogomaya.server.money.Currency;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "specification")
@JsonSubTypes({
    @Type(name = "tictactoe", value = TicTacToeSpecification.class)
 })

public interface GameSpecification extends Serializable {

    public SpecificationName getName();

    public Currency getCurrency();

    public MatchRule getMatchRule();

    public PrivacyRule getPrivacyRule();

    public PlayerNumberRule getNumberRule();

    public BetRule getBetRule();

    public GiveUpRule getGiveUpRule();

    public MoveTimeRule getMoveTimeRule();

    public TotalTimeRule getTotalTimeRule();

}
