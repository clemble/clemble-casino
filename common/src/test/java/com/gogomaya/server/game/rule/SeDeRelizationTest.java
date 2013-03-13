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
import com.gogomaya.server.game.rule.participant.FixedParticipantRule;
import com.gogomaya.server.game.rule.participant.LimitedParticipantRule;
import com.gogomaya.server.game.rule.participant.ParticipantMatchType;
import com.gogomaya.server.game.rule.participant.ParticipantPrivacyType;
import com.gogomaya.server.game.rule.participant.ParticipantRule;
import com.gogomaya.server.game.rule.participant.ParticipantType;
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
        BetRule betRule = objectMapper.readValue("{\"betType\":\"Unlimited\"}", UnlimitedBetRule.class);
        assertTrue(betRule instanceof UnlimitedBetRule);

        betRule = objectMapper.readValue("{\"betType\":\"Fixed\",\"price\":100}", BetRule.class);
        assertTrue(betRule instanceof FixedBetRule);
        Assert.assertEquals(((FixedBetRule) betRule).getPrice(), 100);

        betRule = objectMapper.readValue("{\"betType\":\"Limited\",\"minBet\":100,\"maxBet\":1000}", BetRule.class);
        assertTrue(betRule instanceof LimitedBetRule);
        Assert.assertEquals(((LimitedBetRule) betRule).getMinBet(), 100);
        Assert.assertEquals(((LimitedBetRule) betRule).getMaxBet(), 1000);

        betRule = objectMapper.readValue("{\"betType\":\"Unlimited\"}", UnlimitedBetRule.class);
        assertTrue(betRule instanceof UnlimitedBetRule);

        betRule = objectMapper.readValue("{\"betType\":\"Fixed\",\"price\":100}", BetRule.class);
        assertTrue(betRule instanceof FixedBetRule);
        Assert.assertEquals(((FixedBetRule) betRule).getPrice(), 100);

        betRule = objectMapper.readValue("{\"betType\":\"Limited\",\"minBet\":100,\"maxBet\":1000}", BetRule.class);
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

    @Test
    public void participantRule() throws JsonGenerationException, JsonMappingException, IOException {
        ParticipantRule participantRule = objectMapper.readValue(
                "{\"participantType\":\"Fixed\",\"matchType\":\"Automatic\",\"privacyType\":\"Private\",\"players\":2}", ParticipantRule.class);
        Assert.assertTrue(participantRule instanceof FixedParticipantRule);
        Assert.assertEquals(participantRule.getMatchType(), ParticipantMatchType.Automatic);
        Assert.assertEquals(participantRule.getParticipantType(), ParticipantType.Fixed);
        Assert.assertEquals(participantRule.getPrivacyType(), ParticipantPrivacyType.Private);
        Assert.assertEquals(((FixedParticipantRule) participantRule).getNumberOfParticipants(), 2);
        participantRule = objectMapper.readValue(
                "{\"participantType\":\"Limited\",\"matchType\":\"Automatic\",\"privacyType\":\"Private\",\"minPlayers\":2,\"maxPlayers\":4}",
                ParticipantRule.class);
        Assert.assertTrue(participantRule instanceof LimitedParticipantRule);
        Assert.assertEquals(participantRule.getMatchType(), ParticipantMatchType.Automatic);
        Assert.assertEquals(participantRule.getParticipantType(), ParticipantType.Limited);
        Assert.assertEquals(participantRule.getPrivacyType(), ParticipantPrivacyType.Private);
        Assert.assertEquals(((LimitedParticipantRule) participantRule).getMinPlayers(), 2);
        Assert.assertEquals(((LimitedParticipantRule) participantRule).getMaxPlayers(), 4);
    }

}
