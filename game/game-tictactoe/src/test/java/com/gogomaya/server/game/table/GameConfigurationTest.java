package com.gogomaya.server.game.table;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.game.configuration.TicTacToeConfigurationManager;
import com.gogomaya.server.spring.game.TicTacToeSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { TicTacToeSpringConfiguration.class })
@Transactional
public class GameConfigurationTest {

    @Inject
    TicTacToeConfigurationManager configurationManager;

    @Test
    public void testOptions() {
        GameSpecificationOptions specificationOptions = configurationManager.getSpecificationOptions();
        Assert.assertNotNull(specificationOptions);
        Assert.assertTrue(((SelectSpecificationOptions) specificationOptions).specifications.size() > 0);
    }
}
