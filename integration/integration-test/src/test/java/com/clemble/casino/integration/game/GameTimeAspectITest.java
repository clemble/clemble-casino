package com.clemble.casino.integration.game;

import java.util.List;

import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.integration.util.ClembleCasinoExceptionMatcherFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.game.Game;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.util.RedisCleaner;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
public class GameTimeAspectITest {

    @Autowired
    public GameScenarios gameOperations;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Test
    @Ignore
    public void testMoveTimeout() throws Exception{
        List<RoundGamePlayer<NumberState>> players = gameOperations.round(Game.num);
        RoundGamePlayer playerA = players.get(0);
        RoundGamePlayer playerB = players.get(1);

        Assert.assertNotNull(players);
        Assert.assertEquals(playerA.getSessionKey(), playerB.getSessionKey());
        Assert.assertEquals(players.size(), 2);

        playerA.perform(new SelectNumberEvent(playerA.getPlayer(), 1));
        Thread.sleep(playerA.getConfiguration().getMoveTimeRule().getLimit() + 100);

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.GamePlayGameEnded));
        playerB.perform(new SelectNumberEvent(playerA.getPlayer(), 1));
    }

    @Test
    public void testTotalTimeout() {
//        List<RoundGamePlayer<PoState>> players = gameOperations.match(Game.pic);
//        PoRoundPlayer playerA = (PoRoundPlayer) players.get(0);
//        PoRoundPlayer playerB = (PoRoundPlayer) players.get(1);
//        ClembleCasinoException gogomayaException = null;
//
//        GameConfiguration configuration =  playerA.playerOperations().gameConstructionOperations().getConfigurations().getConfiguration(playerA.getConfigurationKey());
//
//        Assert.assertTrue(configuration.getTotalTimeRule().getLimit() > 0);
//        Assert.assertTrue(configuration.getMoveTimeRule().getLimit() > 0);
//
//        long stepWaitTimeout = 300 + configuration.getTotalTimeRule().getLimit() / 4;
//
//        try {
//
//            Assert.assertNotNull(players);
//            Assert.assertEquals(playerA.getSessionKey(), playerB.getSessionKey());
//            Assert.assertEquals(players.size(), 2);
//
//            playerA.select(0, 0);
//
//            sleep(stepWaitTimeout);
//
//            playerA.bet(1);
//            playerB.bet(1);
//
//            sleep(stepWaitTimeout);
//
//            playerB.select(1, 0);
//
//            sleep(stepWaitTimeout);
//
//            playerA.bet(1);
//            playerB.bet(1);
//
//            playerA.select(0, 1);
//
//            sleep(stepWaitTimeout);
//
//            playerA.bet(1);
//            playerB.bet(1);
//
//        } catch (ClembleCasinoException exception) {
//            gogomayaException = exception;
//        } finally {
//            playerA.close();
//            playerB.close();
//        }

//        Assert.assertNotNull(gogomayaException);
        // TODO restore assertGogomayaFailure(gogomayaException, ClembleCasinoError.GamePlayGameEnded);
    }

}
