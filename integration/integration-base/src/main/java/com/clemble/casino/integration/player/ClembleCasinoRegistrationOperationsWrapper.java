package com.clemble.casino.integration.player;

import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.ClembleCasinoRegistrationOperations;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.player.security.PlayerCredential;

public class ClembleCasinoRegistrationOperationsWrapper extends AbstractTestExecutionListener implements ClembleCasinoRegistrationOperations {

    final private ClembleCasinoRegistrationOperations delegate;
    final private LinkedBlockingQueue<ClembleCasinoOperations> initializedOperations = new LinkedBlockingQueue<>();

    public ClembleCasinoRegistrationOperationsWrapper(ClembleCasinoRegistrationOperations delegate){
        this.delegate = delegate;
    }

    @Override
    public ClembleCasinoOperations login(PlayerCredential playerCredentials) {
        ClembleCasinoOperations casinoOperations = delegate.login(playerCredentials);
        initializedOperations.add(casinoOperations);
        return casinoOperations;
    }

    @Override
    public ClembleCasinoOperations createPlayer(PlayerCredential playerCredential, PlayerProfile playerProfile) {
        ClembleCasinoOperations casinoOperations = delegate.createPlayer(playerCredential, playerProfile);
        initializedOperations.add(casinoOperations);
        return casinoOperations;
    }

    @Override
    public ClembleCasinoOperations createSocialPlayer(PlayerCredential playerCredential, SocialConnectionData socialConnectionData) {
        ClembleCasinoOperations casinoOperations = delegate.createSocialPlayer(playerCredential, socialConnectionData);
        initializedOperations.add(casinoOperations);
        return casinoOperations;
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        ClembleCasinoOperations createdContext;
        while((createdContext = initializedOperations.poll()) != null){
            try {
                createdContext.close();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
