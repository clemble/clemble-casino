package com.gogomaya.server.game.rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.game.bet.rule.BetFixedRule;
import com.gogomaya.server.game.bet.rule.BetLimitedRule;
import com.gogomaya.server.game.bet.rule.BetRule;
import com.gogomaya.server.game.bet.rule.BetUnlimitedRule;
import com.gogomaya.server.game.giveup.rule.GiveUpRule;
import com.gogomaya.server.game.time.rule.TimeBreachBehavior;
import com.gogomaya.server.game.time.rule.TimeLimitMoveRule;
import com.gogomaya.server.game.time.rule.TimeLimitNoneRule;
import com.gogomaya.server.game.time.rule.TimeLimitRule;
import com.gogomaya.server.game.time.rule.TimeLimitTotalRule;
import com.gogomaya.server.spring.common.CommonModuleSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonModuleSpringConfiguration.class })
public class SeDeRelizationTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void timeRule() throws JsonParseException, JsonMappingException, IOException {
        TimeLimitRule timeRule = objectMapper.readValue("{\"timeLimitType\":\"none\"}", TimeLimitRule.class);
        Assert.assertTrue(timeRule instanceof TimeLimitNoneRule);
        Assert.assertEquals(timeRule.getBreachBehavior(), TimeBreachBehavior.nothing);

        timeRule = objectMapper.readValue("{\"timeLimitType\":\"total\",\"punishment\":\"nothing\",\"limit\":1}", TimeLimitRule.class);
        Assert.assertTrue(timeRule instanceof TimeLimitTotalRule);
        Assert.assertEquals(timeRule.getBreachBehavior(), TimeBreachBehavior.nothing);
        Assert.assertEquals(((TimeLimitTotalRule) timeRule).getGameTimeLimit(), 1);

        timeRule = objectMapper.readValue("{\"timeLimitType\":\"total\",\"punishment\":\"loose\",\"limit\":1}", TimeLimitRule.class);
        Assert.assertTrue(timeRule instanceof TimeLimitTotalRule);
        Assert.assertEquals(timeRule.getBreachBehavior(), TimeBreachBehavior.loose);
        Assert.assertEquals(((TimeLimitTotalRule) timeRule).getGameTimeLimit(), 1);

        timeRule = objectMapper.readValue("{\"timeLimitType\":\"move\",\"punishment\":\"nothing\",\"limit\":1}", TimeLimitRule.class);
        Assert.assertTrue(timeRule instanceof TimeLimitMoveRule);
        Assert.assertEquals(timeRule.getBreachBehavior(), TimeBreachBehavior.nothing);
        Assert.assertEquals(((TimeLimitMoveRule) timeRule).getMoveTimeLimit(), 1);

        timeRule = objectMapper.readValue("{\"timeLimitType\":\"move\",\"punishment\":\"loose\",\"limit\":1}", TimeLimitRule.class);
        Assert.assertTrue(timeRule instanceof TimeLimitMoveRule);
        Assert.assertEquals(timeRule.getBreachBehavior(), TimeBreachBehavior.loose);
        Assert.assertEquals(((TimeLimitMoveRule) timeRule).getMoveTimeLimit(), 1);
    }

    @Test
    public void betRule() throws JsonGenerationException, JsonMappingException, IOException {
        BetRule betRule = objectMapper.readValue("{\"betType\":\"unlimited\"}", BetRule.class);
        assertTrue(betRule instanceof BetUnlimitedRule);
        assertEquals(betRule, BetUnlimitedRule.INSTANCE);

        betRule = objectMapper.readValue("{\"betType\":\"fixed\",\"price\":100}", BetRule.class);
        assertTrue(betRule instanceof BetFixedRule);

        Assert.assertEquals(((BetFixedRule) betRule).getPrice(), 100);

        betRule = objectMapper.readValue("{\"betType\":\"limited\",\"minBet\":100,\"maxBet\":1000}", BetRule.class);
        assertTrue(betRule instanceof BetLimitedRule);
        Assert.assertEquals(((BetLimitedRule) betRule).getMinBet(), 100);
        Assert.assertEquals(((BetLimitedRule) betRule).getMaxBet(), 1000);

        betRule = objectMapper.readValue("{\"betType\":\"unlimited\"}", BetRule.class);
        assertTrue(betRule instanceof BetUnlimitedRule);

        betRule = objectMapper.readValue("{\"betType\":\"fixed\",\"price\":100}", BetRule.class);
        assertTrue(betRule instanceof BetFixedRule);
        Assert.assertEquals(((BetFixedRule) betRule).getPrice(), 100);

        betRule = objectMapper.readValue("{\"betType\":\"limited\",\"minBet\":100,\"maxBet\":1000}", BetRule.class);
        assertTrue(betRule instanceof BetLimitedRule);
        Assert.assertEquals(((BetLimitedRule) betRule).getMinBet(), 100);
        Assert.assertEquals(((BetLimitedRule) betRule).getMaxBet(), 1000);
    }

    @Test
    public void giveUpRule() throws JsonGenerationException, JsonMappingException, IOException {
        GiveUpRule giveUpRule = objectMapper.readValue("\"all\"", GiveUpRule.class);
        Assert.assertEquals(giveUpRule, GiveUpRule.all);

        giveUpRule = objectMapper.readValue("\"lost\"", GiveUpRule.class);
        Assert.assertEquals(giveUpRule, GiveUpRule.lost);
    }

}
