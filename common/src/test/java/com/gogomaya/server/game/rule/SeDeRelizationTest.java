package com.gogomaya.server.game.rule;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

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

import com.gogomaya.server.game.rule.bet.BetRule;
import com.gogomaya.server.game.rule.bet.BetFixedRule;
import com.gogomaya.server.game.rule.bet.BetLimitedRule;
import com.gogomaya.server.game.rule.bet.BetUnlimitedRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.giveup.GiveUpAllRule;
import com.gogomaya.server.game.rule.giveup.GiveUpLostRule;
import com.gogomaya.server.game.rule.giveup.GiveUpLeastRule;
import com.gogomaya.server.game.rule.time.LimitedGameTimeRule;
import com.gogomaya.server.game.rule.time.LimitedMoveTimeRule;
import com.gogomaya.server.game.rule.time.TimeBreachBehavior;
import com.gogomaya.server.game.rule.time.TimeRule;
import com.gogomaya.server.game.rule.time.UnlimitedTimeRule;
import com.gogomaya.server.spring.common.CommonModuleSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonModuleSpringConfiguration.class })
public class SeDeRelizationTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void timeRule() throws JsonParseException, JsonMappingException, IOException {
        TimeRule timeRule = objectMapper.readValue("{\"timeType\":\"Unlimited\",\"timeBreach\":\"DoNothing\"}", TimeRule.class);
        Assert.assertTrue(timeRule instanceof UnlimitedTimeRule);
        Assert.assertEquals(timeRule.getBreachBehavior(), TimeBreachBehavior.DoNothing);

        timeRule = objectMapper.readValue("{\"timeType\":\"Unlimited\",\"timeBreach\":\"PlayerLoose\"}", TimeRule.class);
        Assert.assertTrue(timeRule instanceof UnlimitedTimeRule);
        Assert.assertEquals(timeRule.getBreachBehavior(), TimeBreachBehavior.DoNothing);

        timeRule = objectMapper.readValue("{\"timeType\":\"LimitedGameTime\",\"timeBreach\":\"DoNothing\",\"timeLimit\":1}", TimeRule.class);
        Assert.assertTrue(timeRule instanceof LimitedGameTimeRule);
        Assert.assertEquals(timeRule.getBreachBehavior(), TimeBreachBehavior.DoNothing);
        Assert.assertEquals(((LimitedGameTimeRule) timeRule).getGameTimeLimit(), 1);

        timeRule = objectMapper.readValue("{\"timeType\":\"LimitedGameTime\",\"timeBreach\":\"PlayerLoose\",\"timeLimit\":1}", TimeRule.class);
        Assert.assertTrue(timeRule instanceof LimitedGameTimeRule);
        Assert.assertEquals(timeRule.getBreachBehavior(), TimeBreachBehavior.PlayerLoose);
        Assert.assertEquals(((LimitedGameTimeRule) timeRule).getGameTimeLimit(), 1);

        timeRule = objectMapper.readValue("{\"timeType\":\"LimitedMoveTime\",\"timeBreach\":\"DoNothing\",\"timeLimit\":1}", TimeRule.class);
        Assert.assertTrue(timeRule instanceof LimitedMoveTimeRule);
        Assert.assertEquals(timeRule.getBreachBehavior(), TimeBreachBehavior.DoNothing);
        Assert.assertEquals(((LimitedMoveTimeRule) timeRule).getMoveTimeLimit(), 1);

        timeRule = objectMapper.readValue("{\"timeType\":\"LimitedMoveTime\",\"timeBreach\":\"PlayerLoose\",\"timeLimit\":1}", TimeRule.class);
        Assert.assertTrue(timeRule instanceof LimitedMoveTimeRule);
        Assert.assertEquals(timeRule.getBreachBehavior(), TimeBreachBehavior.PlayerLoose);
        Assert.assertEquals(((LimitedMoveTimeRule) timeRule).getMoveTimeLimit(), 1);
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
        GiveUpRule giveUpRule = objectMapper.readValue("{\"looseType\":\"all\"}", GiveUpRule.class);
        Assert.assertEquals(giveUpRule, GiveUpAllRule.INSTANCE);

        giveUpRule = objectMapper.readValue("{\"looseType\":\"lost\"}", GiveUpRule.class);
        Assert.assertEquals(giveUpRule, GiveUpLostRule.INSTANCE);

        giveUpRule = objectMapper.readValue("{\"looseType\":\"min\",\"min\":5}", GiveUpRule.class);
        Assert.assertTrue(giveUpRule instanceof GiveUpLeastRule);
        Assert.assertEquals(((GiveUpLeastRule) giveUpRule).getMinPart(), 5);
    }

}
