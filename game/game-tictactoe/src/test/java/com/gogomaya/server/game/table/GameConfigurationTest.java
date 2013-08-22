package com.gogomaya.server.game.table;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gogomaya.server.game.configuration.GameSpecificationConfigurationManager;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.tictactoe.PicPacPoeSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(SpringConfiguration.PROFILE_TEST)
@ContextConfiguration(classes = { PicPacPoeSpringConfiguration.class })
@Transactional
public class GameConfigurationTest {

    @Inject
    GameSpecificationConfigurationManager ticTacToeConfigurationManager;

    @Test
    public void testOptions() {
        GameSpecificationOptions specificationOptions = ticTacToeConfigurationManager.getSpecificationOptions();
        Assert.assertNotNull(specificationOptions);
        Assert.assertTrue(((SelectSpecificationOptions) specificationOptions).getSpecifications().size() > 0);
    }
}
