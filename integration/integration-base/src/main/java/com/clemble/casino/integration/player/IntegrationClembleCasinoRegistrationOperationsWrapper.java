package com.clemble.casino.integration.player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import com.clemble.casino.registration.PlayerLoginRequest;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.ClembleCasinoRegistrationOperations;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.social.SocialAccessGrant;
import com.clemble.casino.social.SocialConnectionData;
import com.clemble.casino.registration.PlayerCredential;

public class IntegrationClembleCasinoRegistrationOperationsWrapper extends AbstractTestExecutionListener implements ClembleCasinoRegistrationOperations {

    final private static AtomicReference<ClembleCasinoRegistrationOperations> delegate = new AtomicReference<>();
    final private static LinkedBlockingQueue<ClembleCasinoOperations> initializedOperations = new LinkedBlockingQueue<>();

    public IntegrationClembleCasinoRegistrationOperationsWrapper(){
    }

    public IntegrationClembleCasinoRegistrationOperationsWrapper(ClembleCasinoRegistrationOperations delegate){
        this.delegate.set(delegate);
    }

    @Override
    public ClembleCasinoOperations login(PlayerLoginRequest playerCredentials) {
        ClembleCasinoOperations casinoOperations = delegate.get().login(playerCredentials);
        initializedOperations.add(casinoOperations);
        return casinoOperations;
    }

    @Override
    public ClembleCasinoOperations register(PlayerCredential playerCredential, PlayerProfile playerProfile) {
        ClembleCasinoOperations casinoOperations = delegate.get().register(playerCredential, playerProfile);
        initializedOperations.add(casinoOperations);
        return casinoOperations;
    }

    @Override
    public ClembleCasinoOperations register(PlayerCredential playerCredential, SocialConnectionData socialConnectionData) {
        ClembleCasinoOperations casinoOperations = delegate.get().register(playerCredential, socialConnectionData);
        initializedOperations.add(casinoOperations);
        return casinoOperations;
    }

    @Override
    public ClembleCasinoOperations register(PlayerCredential playerCredential, SocialAccessGrant accessGrant) {
        ClembleCasinoOperations casinoOperations = delegate.get().register(playerCredential, accessGrant);
        initializedOperations.add(casinoOperations);
        return casinoOperations;
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        ClembleCasinoOperations createdContext = null;
        while((createdContext = initializedOperations.poll()) != null){
            try {
                createdContext.close();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

}
