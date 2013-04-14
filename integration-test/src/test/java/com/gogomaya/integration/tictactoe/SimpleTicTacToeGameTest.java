package com.gogomaya.integration.tictactoe;

import java.util.Collection;

import javax.inject.Inject;

import org.jbehave.core.annotations.UsingSteps;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.integration.tictactoe.TicTacToeOperations;
import com.gogomaya.server.integration.tictactoe.TicTacToePlayer;
import com.gogomaya.server.spring.integration.IntegrationTestConfiguration;
import com.gogomaya.tests.validation.PlayerCredentialsValidation;

@RunWith(SpringJUnit4ClassRunner.class)
@UsingSteps(instances = PlayerCredentialsValidation.class)
@ContextConfiguration(classes = { IntegrationTestConfiguration.class })
public class SimpleTicTacToeGameTest {

    @Inject
    TicTacToeOperations ticTacToeOperations;

    @Test
    public void testGameStart() {
        Collection<TicTacToePlayer> players = ticTacToeOperations.start();

        Assert.assertNotNull(players);
        Assert.assertNotNull(players.iterator().next());
        Assert.assertEquals(players.size(), 2);
    }

}
