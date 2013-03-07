package com.gogomaya.server.game.rule;

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

import com.gogomaya.server.game.rule.bet.BetRule;
import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.rule.bet.LimitedBetRule;
import com.gogomaya.server.game.rule.bet.UnlimitedBetRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.giveup.LooseAllGiveUpRule;
import com.gogomaya.server.game.rule.giveup.LooseLostGiveUpRule;
import com.gogomaya.server.game.rule.giveup.LooseMinGiveUpRule;
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
        Assert.assertEquals(timeRule.getBreachBehavior(), TimeBreachBehavior.PlayerLoose);

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
        BetRule betRule = objectMapper.readValue("{\"betType\":\"Unlimited\", \"cashType\":\"FakeMoney\"}", UnlimitedBetRule.class);
        assertTrue(betRule instanceof UnlimitedBetRule);

        betRule = objectMapper.readValue("{\"betType\":\"Fixed\", \"cashType\":\"FakeMoney\",\"price\":100}", BetRule.class);
        assertTrue(betRule instanceof FixedBetRule);
        Assert.assertEquals(((FixedBetRule) betRule).getPrice(), 100);

        betRule = objectMapper.readValue("{\"betType\":\"Limited\", \"cashType\":\"FakeMoney\",\"minBet\":100,\"maxBet\":1000}", BetRule.class);
        assertTrue(betRule instanceof LimitedBetRule);
        Assert.assertEquals(((LimitedBetRule) betRule).getMinBet(), 100);
        Assert.assertEquals(((LimitedBetRule) betRule).getMaxBet(), 1000);
    }
    
    @Test
    public void giveUpRule() throws JsonGenerationException, JsonMappingException, IOException {
        GiveUpRule giveUpRule = objectMapper.readValue("{\"looseType\":\"All\"}", GiveUpRule.class);
        Assert.assertEquals(giveUpRule, LooseAllGiveUpRule.INSTANCE);
        
        giveUpRule = objectMapper.readValue("{\"looseType\":\"Lost\"}", GiveUpRule.class);
        Assert.assertEquals(giveUpRule, LooseLostGiveUpRule.INSTANCE);
        
        giveUpRule = objectMapper.readValue("{\"looseType\":\"MinPart\",\"minPart\":5}", GiveUpRule.class);
        Assert.assertTrue(giveUpRule instanceof LooseMinGiveUpRule);
        Assert.assertEquals(((LooseMinGiveUpRule) giveUpRule).getMinPart(), 5);
    }

}
