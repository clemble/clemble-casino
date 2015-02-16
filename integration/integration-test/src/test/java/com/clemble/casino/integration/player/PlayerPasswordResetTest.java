package com.clemble.casino.integration.player;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.client.event.PlayerEventSelector;
import com.clemble.casino.event.Event;
import com.clemble.casino.integration.ClembleIntegrationTest;
import com.clemble.casino.integration.event.EventAccumulator;
import com.clemble.casino.integration.game.construction.EmailScenarios;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.registration.PlayerCredential;
import com.clemble.casino.registration.PlayerLoginRequest;
import com.clemble.casino.registration.PlayerPasswordResetRequest;
import com.clemble.casino.registration.PlayerPasswordRestoreRequest;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.event.email.SystemEmailSendRequestEvent;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.junit.Assert;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by mavarazy on 2/2/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class PlayerPasswordResetTest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Autowired
    public EmailScenarios emailScenarios;

    @Autowired
    @Qualifier("systemEventAccumulator")
    public EventAccumulator<SystemEvent> systemEventAccumulator;

    @Test
    public void testPlayerPasswordChange(){
        // Step 1. Creating player
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        String player = A.getPlayer();
        emailScenarios.verify(A);
        String emailA = A.emailService().myEmail();
        Assert.assertNotNull(emailA);
        // Step 2. Restoring email
        A.passwordResetService().restore(new PlayerPasswordRestoreRequest(emailA));
        // Step 3. Checking reset would update password
        String newPassword = RandomStringUtils.randomAlphanumeric(20);
        // Step 4. Fetching update event
        SystemEmailSendRequestEvent emailRequest = systemEventAccumulator.waitFor(EventSelectors.
            where(new EventTypeSelector(SystemEmailSendRequestEvent.class)).
            and(new PlayerEventSelector(A.getPlayer())).
            and(new EventSelector() {
                @Override
                public boolean filter(Event event) {
                    return ((SystemEmailSendRequestEvent) event).getTemplate().equals("restore_password");
                }
            }));
        String url = emailRequest.getParams().get("url");
        String token = url.substring(url.lastIndexOf("/") + 1);
        // Step 5. Generating new password
        PlayerPasswordResetRequest passwordResetRequest = new PlayerPasswordResetRequest(token, newPassword);
        A.passwordResetService().reset(passwordResetRequest);
        A.signOut();
        // Step 6. Checking login with new password works
        ClembleCasinoOperations A1 = playerScenarios.login(new PlayerLoginRequest(null, emailA, newPassword));
        Assert.assertNotNull(A1);
        Assert.assertEquals(A1.getPlayer(), player);
    }

}
