package com.clemble.casino.integration.player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.ClembleCasinoRegistrationOperations;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.social.SocialAccessGrant;
import com.clemble.casino.social.SocialConnectionData;
import com.clemble.casino.registration.PlayerCredential;

public class ClembleCasinoRegistrationOperationsWrapper extends AbstractTestExecutionListener implements ClembleCasinoRegistrationOperations {

    final private static AtomicReference<ClembleCasinoRegistrationOperations> delegate = new AtomicReference<>();
    final private static LinkedBlockingQueue<ClembleCasinoOperations> initializedOperations = new LinkedBlockingQueue<>();

    public ClembleCasinoRegistrationOperationsWrapper(){
    }

    public ClembleCasinoRegistrationOperationsWrapper(ClembleCasinoRegistrationOperations delegate){
        this.delegate.set(delegate);
    }

    @Override
    public ClembleCasinoOperations login(PlayerCredential playerCredentials) {
        ClembleCasinoOperations casinoOperations = delegate.get().login(playerCredentials);
        initializedOperations.add(casinoOperations);
        return casinoOperations;
    }

    @Override
    public ClembleCasinoOperations createPlayer(PlayerCredential playerCredential, PlayerProfile playerProfile) {
        ClembleCasinoOperations casinoOperations = delegate.get().createPlayer(playerCredential, playerProfile);
        initializedOperations.add(casinoOperations);
        return casinoOperations;
    }

    @Override
    public ClembleCasinoOperations createSocialPlayer(PlayerCredential playerCredential, SocialConnectionData socialConnectionData) {
        ClembleCasinoOperations casinoOperations = delegate.get().createSocialPlayer(playerCredential, socialConnectionData);
        initializedOperations.add(casinoOperations);
        return casinoOperations;
    }

    @Override
    public ClembleCasinoOperations createSocialPlayer(PlayerCredential playerCredential, SocialAccessGrant accessGrant) {
        ClembleCasinoOperations casinoOperations = delegate.get().createSocialPlayer(playerCredential, accessGrant);
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
