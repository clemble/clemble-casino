package com.clemble.casino.integration.player;

import com.clemble.casino.android.player.AndroidFacadeRegistrationService;
import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.integration.ClembleIntegrationTest;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.registration.service.FacadeRegistrationService;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.test.util.ClembleCasinoExceptionMatcherFactory;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by mavarazy on 1/14/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class PlayerSignOutITest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    public PlayerScenarios playerScenarios;

    @Autowired(required = false)
    public FacadeRegistrationService facadeRegistrationService;

    @Test
    @IfProfileValue(name = SpringConfiguration.INTEGRATION_TEST)
    public void testSignOut() {
        if (!(facadeRegistrationService instanceof AndroidFacadeRegistrationService))
            return;
        // Step 1. Creating player
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 2. Check that operations are forbidden, after signOut
        Assert.assertNotNull(A.profileOperations().myProfile());
        // Step 2.1. Sign out from application
        A.signOut();
        // Step 3. Checking profile operation is no longer allowed
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.ServerError));
        A.profileOperations().myProfile();
    }

}
