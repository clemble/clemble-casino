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
import com.gogomaya.server.game.match.GameStateManager;
import com.gogomaya.server.game.rule.GameRuleSpecification;
import com.gogomaya.server.game.rule.bet.BetFixedRule;
import com.gogomaya.server.game.rule.giveup.GiveUpAllRule;
import com.gogomaya.server.game.rule.time.UnlimitedTimeRule;
import com.gogomaya.server.game.table.rule.GameTableSpecification;
import com.gogomaya.server.game.table.rule.PlayerMatchRule;
import com.gogomaya.server.game.table.rule.PlayerNumberRule;
import com.gogomaya.server.game.table.rule.PlayerPrivacyRule;
import com.gogomaya.server.player.wallet.CashType;
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
        GameSpecification specification = GameSpecification.create(
                GameTableSpecification.create(PlayerMatchRule.Automatic, PlayerPrivacyRule.Public, PlayerNumberRule.create(2, 2)),
                GameRuleSpecification.create(CashType.FakeMoney, BetFixedRule.create(50), GiveUpAllRule.INSTANCE, UnlimitedTimeRule.INSTANCE));

        GameTable table = gameStateManager.reserve(1, specification);
        
        Assert.assertEquals(table.getSpecification(), specification);

        GameTable anotherTable = gameStateManager.reserve(2, specification);

        Assert.assertEquals(table.getTableId(), anotherTable.getTableId());
    }
}
