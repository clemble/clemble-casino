package com.clemble.casino.integration.goal;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.client.goal.GoalOperations;
import com.clemble.casino.client.payment.PaymentCompleteEventSelector;
import com.clemble.casino.event.Event;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.construction.GoalConstruction;
import com.clemble.casino.goal.lifecycle.construction.GoalConstructionRequest;
import com.clemble.casino.goal.lifecycle.construction.GoalSuggestion;
import com.clemble.casino.goal.lifecycle.construction.GoalSuggestionRequest;
import com.clemble.casino.integration.ClembleIntegrationTest;
import com.clemble.casino.integration.event.EventAccumulator;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.integration.utils.CheckUtils;
import com.clemble.casino.lifecycle.management.event.action.surrender.GiveUpAction;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.payment.event.PaymentCompleteEvent;
import com.google.common.collect.ImmutableList;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by mavarazy on 1/15/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class GoalSurrenderITest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Test
    public void testSurrenderTransaction() {
        // Step 1. Create player
        final ClembleCasinoOperations A = playerScenarios.createPlayer();
        final GoalOperations AgoalOps = A.goalOperations();
        // Step 2. Create a goal
        final GoalConfiguration Aconf = AgoalOps.configurationService().getConfigurations().get(0);
        final GoalConstruction AgoalConst = AgoalOps.constructionService().construct(new GoalConstructionRequest(Aconf, "A conf", "", DateTimeZone.UTC));
        final String goalKey = AgoalConst.getGoalKey();
        CheckUtils.check((i) -> AgoalOps.actionService().getState(goalKey) != null);
        EventAccumulator<PaymentCompleteEvent> paymentAccumulator = new EventAccumulator<PaymentCompleteEvent>();
        A.listenerOperations().subscribe(new EventTypeSelector(PaymentCompleteEvent.class), paymentAccumulator);
        // Step 3. Checking value before give up
        Money beforeGiveUp = A.accountService().myAccount().getMoney(Currency.FakeMoney);
        // Step 4. Giving up
        GiveUpAction action = new GiveUpAction();
        AgoalOps.actionService().process(goalKey, action);
        // Step 5. Checking value processed
        PaymentCompleteEvent completeEvent = paymentAccumulator.waitFor(new PaymentCompleteEventSelector(goalKey));
        Assert.assertNotNull(completeEvent);
        // Step 6. Checking amount did not change
        Money afterGiveUp = A.accountService().myAccount().getMoney(Currency.FakeMoney);
        Assert.assertEquals(beforeGiveUp, afterGiveUp);
    }

}
