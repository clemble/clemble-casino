package com.gogomaya.server.game.table;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.bet.rule.BetFixedRule;
import com.gogomaya.server.game.giveup.rule.GiveUpRule;
import com.gogomaya.server.game.match.GameStateManager;
import com.gogomaya.server.game.table.rule.GameTableMatchRule;
import com.gogomaya.server.game.table.rule.GameTablePlayerNumberRule;
import com.gogomaya.server.game.table.rule.GameTablePrivacyRule;
import com.gogomaya.server.game.time.rule.TimeLimitNoneRule;
import com.gogomaya.server.money.Currency;
import com.gogomaya.server.spring.game.GameManagementSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { GameManagementSpringConfiguration.class })
@Transactional
public class GameTableManagerTest {
    
    @Autowired
    private GameStateManager gameStateManager;

    @Test
    public void testPlayersMapping(){
        GameSpecification specification = GameSpecification.create(Currency.FakeMoney, BetFixedRule.create(50), GiveUpRule.DEFAULT, TimeLimitNoneRule.INSTANCE, GameTableMatchRule.automatic, GameTablePrivacyRule.all, GameTablePlayerNumberRule.create(2, 2));

        GameTable table = gameStateManager.reserve(1, specification);
        
        Assert.assertEquals(table.getSpecification(), specification);

        GameTable anotherTable = gameStateManager.reserve(2, specification);

        Assert.assertEquals(table.getTableId(), anotherTable.getTableId());
    }
}
