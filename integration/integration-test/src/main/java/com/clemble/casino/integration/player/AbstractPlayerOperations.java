package com.clemble.casino.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.clemble.casino.integration.game.construction.GameConstructionOperations;
import com.clemble.casino.integration.player.account.AccountOperations;
import com.clemble.casino.integration.player.listener.PlayerListenerOperations;
import com.clemble.casino.integration.player.profile.ProfileOperations;
import com.clemble.casino.integration.player.session.SessionOperations;
import com.clemble.casino.player.NativePlayerProfile;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerIdentity;
import com.clemble.casino.player.web.PlayerRegistrationRequest;

abstract public class AbstractPlayerOperations implements PlayerOperations, ApplicationContextAware {

    final private ProfileOperations profileOperations;
    final private SessionOperations sessionOperations;
    final private AccountOperations accountOperations;
    final private PlayerListenerOperations listenerOperations;
    final private Set<GameConstructionOperations<?>> gameConstructionOperations = new HashSet<>();

    protected AbstractPlayerOperations(PlayerListenerOperations listenerOperations,
            ProfileOperations profileOperations,
            SessionOperations sessionOperations,
            AccountOperations accountOperations) {
        this.listenerOperations = checkNotNull(listenerOperations);
        this.sessionOperations = checkNotNull(sessionOperations);
        this.profileOperations = checkNotNull(profileOperations);
        this.accountOperations = checkNotNull(accountOperations);
    }

	@Override
	@SuppressWarnings("unchecked")
    final public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        gameConstructionOperations.addAll((Collection<? extends GameConstructionOperations<?>>)(Collection<?>) applicationContext.getBeansOfType(GameConstructionOperations.class).values());
    }

    @Override
    final public Player createPlayer() {
        return createPlayer(new NativePlayerProfile().setFirstName(RandomStringUtils.randomAlphabetic(10)).setLastName(RandomStringUtils.randomAlphabetic(10))
                .setNickName(RandomStringUtils.randomAlphabetic(10)));
    }

    @Override
    final public Player createPlayer(PlayerProfile playerProfile) {
        // Step 0. Sanity check
        checkNotNull(playerProfile);
        // Step 1. Creating RegistrationRequest for processing
        PlayerCredential playerCredential = new PlayerCredential().setEmail(RandomStringUtils.randomAlphabetic(30) + "@gmail.com").setPassword(
                UUID.randomUUID().toString());
        PlayerIdentity playerIdentity = new PlayerIdentity()
            .setSecret(UUID.randomUUID().toString())
            .setDevice(UUID.randomUUID().toString());
        PlayerRegistrationRequest registrationRequest = new PlayerRegistrationRequest(playerProfile, playerCredential, playerIdentity);
        // Step 2. Forwarding to appropriate method for processing
        return createPlayer(registrationRequest);
    }

    final public Player create(PlayerIdentity playerIdentity, PlayerCredential credential) {
        return new Player(playerIdentity, credential, profileOperations, sessionOperations, accountOperations, listenerOperations, gameConstructionOperations);
    }

}
