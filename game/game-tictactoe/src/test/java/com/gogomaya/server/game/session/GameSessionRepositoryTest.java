package com.gogomaya.server.game.session;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.game.Game;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.construct.GameInitiation;
import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.rule.construct.PlayerNumberRule;
import com.gogomaya.server.game.rule.construct.PrivacyRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TotalTimeRule;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.specification.SpecificationName;
import com.gogomaya.server.money.Currency;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.repository.game.GameSessionRepository;
import com.gogomaya.server.repository.game.GameSpecificationRepository;
import com.gogomaya.server.repository.game.GameTableRepository;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.tictactoe.PicPacPoeSpringConfiguration;
import com.gogomaya.server.tictactoe.PicPacPoeState;
import com.gogomaya.server.tictactoe.PicPacPoeStateFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(SpringConfiguration.UNIT_TEST)
@ContextConfiguration(classes = { PicPacPoeSpringConfiguration.class })
public class GameSessionRepositoryTest {

    GameSpecification DEFAULT_SPECIFICATION = new GameSpecification().setName(new SpecificationName(Game.pic, "DEFAULT")).setBetRule(FixedBetRule.DEFAULT)
            .setPrice(Money.create(Currency.FakeMoney, 50)).setGiveUpRule(GiveUpRule.lost).setMoveTimeRule(MoveTimeRule.DEFAULT)
            .setTotalTimeRule(TotalTimeRule.DEFAULT).setNumberRule(PlayerNumberRule.two).setPrivacayRule(PrivacyRule.everybody);;

    @Inject
    GameSessionRepository<PicPacPoeState> sessionRepository;

    @Inject
    GameTableRepository<PicPacPoeState> tableRepository;

    @Inject
    PicPacPoeStateFactory stateFactory;

    @Inject
    GameSpecificationRepository specificationRepository;

    @Before
    public void setUp() {
        specificationRepository.saveAndFlush(DEFAULT_SPECIFICATION);
    }

    @Test
    public void testSaveSessionWithState() {
        List<Long> players = new ArrayList<Long>();
        players.add(1L);
        players.add(2L);
        GameInitiation initiation = new GameInitiation(Game.pic, 0, players, DEFAULT_SPECIFICATION);
        PicPacPoeState gameState = stateFactory.constructState(initiation);

        GameTable<PicPacPoeState> gameTable = new GameTable<PicPacPoeState>();
        gameTable.setSpecification(DEFAULT_SPECIFICATION);
        gameTable.setPlayers(players);

        gameTable.setCurrentSession(new GameSession<PicPacPoeState>());

        gameTable = tableRepository.save(gameTable);

        Assert.assertNotNull(gameTable.getCurrentSession());
        gameTable.getCurrentSession().setState(gameState);

        GameSession<PicPacPoeState> gameSession = new GameSession<PicPacPoeState>();
        gameSession.setPlayers(players);
        gameSession.setSpecification(DEFAULT_SPECIFICATION);

        gameSession = sessionRepository.save(gameSession);

        Assert.assertNotNull(gameSession);
        Assert.assertNotNull(gameTable.getCurrentSession().getState());
    }
}
