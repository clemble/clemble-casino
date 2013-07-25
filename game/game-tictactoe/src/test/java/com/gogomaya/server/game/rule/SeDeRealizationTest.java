package com.gogomaya.server.game.rule;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaFailure;
import com.gogomaya.server.game.cell.Cell;
import com.gogomaya.server.game.event.client.BetEvent;
import com.gogomaya.server.game.event.client.generic.SelectCellEvent;
import com.gogomaya.server.game.event.client.surrender.GiveUpEvent;
import com.gogomaya.server.game.rule.bet.BetRule;
import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.rule.bet.LimitedBetRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TimeBreachPunishment;
import com.gogomaya.server.game.rule.time.TimeRule;
import com.gogomaya.server.game.rule.time.TotalTimeRule;
import com.gogomaya.server.spring.common.JsonSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JsonSpringConfiguration.class })
public class SeDeRealizationTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void timeRule() throws JsonParseException, JsonMappingException, IOException {
        TimeRule timeRule = objectMapper.readValue("{\"rule\":\"totalTime\", \"punishment\":\"loose\",\"limit\":1}", TotalTimeRule.class);
        Assert.assertTrue(timeRule instanceof TotalTimeRule);
        Assert.assertEquals(timeRule.getPunishment(), TimeBreachPunishment.loose);
        Assert.assertEquals(((TotalTimeRule) timeRule).getLimit(), 1);

        timeRule = objectMapper.readValue("{\"rule\":\"totalTime\", \"punishment\":\"loose\",\"limit\":1}", TotalTimeRule.class);
        Assert.assertTrue(timeRule instanceof TotalTimeRule);
        Assert.assertEquals(timeRule.getPunishment(), TimeBreachPunishment.loose);
        Assert.assertEquals(((TotalTimeRule) timeRule).getLimit(), 1);

        timeRule = objectMapper.readValue("{\"rule\":\"moveTime\", \"punishment\":\"loose\",\"limit\":1}", MoveTimeRule.class);
        Assert.assertTrue(timeRule instanceof MoveTimeRule);
        Assert.assertEquals(timeRule.getPunishment(), TimeBreachPunishment.loose);
        Assert.assertEquals(((MoveTimeRule) timeRule).getLimit(), 1);

        timeRule = objectMapper.readValue("{\"rule\":\"moveTime\", \"punishment\":\"loose\",\"limit\":1}", MoveTimeRule.class);
        Assert.assertTrue(timeRule instanceof MoveTimeRule);
        Assert.assertEquals(timeRule.getPunishment(), TimeBreachPunishment.loose);
        Assert.assertEquals(((MoveTimeRule) timeRule).getLimit(), 1);
    }

    @Test
    public void betRule() throws JsonGenerationException, JsonMappingException, IOException {
        BetRule betRule = objectMapper.readValue("{\"betType\":\"fixed\",\"bets\": [100] }", BetRule.class);
        assertTrue(betRule instanceof FixedBetRule);

        Assert.assertEquals(((FixedBetRule) betRule).getBets()[0], 100);

        betRule = objectMapper.readValue("{\"betType\":\"limited\",\"minBet\":100,\"maxBet\":1000}", BetRule.class);
        assertTrue(betRule instanceof LimitedBetRule);
        Assert.assertEquals(((LimitedBetRule) betRule).getMinBet(), 100);
        Assert.assertEquals(((LimitedBetRule) betRule).getMaxBet(), 1000);

        betRule = objectMapper.readValue("{\"betType\":\"fixed\",\"bets\":[100]}", BetRule.class);
        assertTrue(betRule instanceof FixedBetRule);
        Assert.assertEquals(((FixedBetRule) betRule).getBets()[0], 100);

        betRule = objectMapper.readValue("{\"betType\":\"limited\",\"minBet\":100,\"maxBet\":1000}", BetRule.class);
        assertTrue(betRule instanceof LimitedBetRule);
        Assert.assertEquals(((LimitedBetRule) betRule).getMinBet(), 100);
        Assert.assertEquals(((LimitedBetRule) betRule).getMaxBet(), 1000);
    }

    @Test
    public void giveUpRule() throws JsonGenerationException, JsonMappingException, IOException {
        GiveUpRule giveUpRule = objectMapper.readValue("{\"giveUp\": \"all\"}", GiveUpRule.class);
        Assert.assertEquals(giveUpRule, GiveUpRule.all);

        giveUpRule = objectMapper.readValue("{\"giveUp\": \"lost\"}", GiveUpRule.class);
        Assert.assertEquals(giveUpRule, GiveUpRule.lost);
    }

    @Test
    public void testSelectCell() throws JsonGenerationException, JsonMappingException, IOException {
        SelectCellEvent selectCellMove = new SelectCellEvent(1, Cell.create(0, 0));

        String value = objectMapper.writeValueAsString(selectCellMove);

        SelectCellEvent parsedSelectCellMove = objectMapper.readValue(value, SelectCellEvent.class);
        Assert.assertEquals(selectCellMove, parsedSelectCellMove);

        System.out.println(value);
    }

    @Test
    public void testBetOnCell() throws JsonGenerationException, JsonMappingException, IOException {
        BetEvent betOnCellMove = new BetEvent(1, 60);

        String value = objectMapper.writeValueAsString(betOnCellMove);

        BetEvent parsedBetOnCellMove = objectMapper.readValue(value, BetEvent.class);
        Assert.assertEquals(betOnCellMove, parsedBetOnCellMove);

        System.out.println(value);
    }

    @Test
    public void testGiveUp() throws IOException {
        GiveUpEvent giveUpMove = new GiveUpEvent(10);

        String value = objectMapper.writeValueAsString(giveUpMove);
        GiveUpEvent parsedBetOnCellMove = objectMapper.readValue(value, GiveUpEvent.class);
        Assert.assertEquals(giveUpMove, parsedBetOnCellMove);

        System.out.println(value);
    }

    @Test
    public void testErrorSeDeRealization() throws Throwable {
        GogomayaFailure failure = new GogomayaFailure(GogomayaError.BadRequestPlayerIdHeaderMissing);

        String value = objectMapper.writeValueAsString(failure);
        System.out.println(value);
        GogomayaFailure deserializedFailure = objectMapper.readValue(value, GogomayaFailure.class);
        Assert.assertEquals(failure, deserializedFailure);
    }

}
