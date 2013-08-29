package com.gogomaya.server.player.registration;

import java.util.UUID;

import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.gogomaya.player.PlayerProfile;
import com.gogomaya.player.security.PlayerCredential;
import com.gogomaya.player.security.PlayerIdentity;
import com.gogomaya.player.web.RegistrationRequest;
import com.gogomaya.server.player.account.PlayerAccountService;
import com.gogomaya.server.repository.player.PlayerCredentialRepository;
import com.gogomaya.server.repository.player.PlayerIdentityRepository;
import com.gogomaya.server.repository.player.PlayerProfileRepository;

public class PlayerRegistrationService {

    final private PlayerAccountService playerAccountService;
    final private PlayerProfileRepository playerProfileRepository;
    final private PlayerIdentityRepository playerIdentityRepository;
    final private PlayerCredentialRepository playerCredentialRepository;

    public PlayerRegistrationService(final PlayerProfileRepository playerProfileRepository,
            final PlayerIdentityRepository playerIdentityRepository,
            final PlayerCredentialRepository playerCredentialRepository,
            final PlayerAccountService playerAccountService) {
        this.playerCredentialRepository = playerCredentialRepository;
        this.playerIdentityRepository = playerIdentityRepository;
        this.playerProfileRepository = playerProfileRepository;
        this.playerAccountService = playerAccountService;
    }

    public PlayerIdentity register(final RegistrationRequest registrationRequest) {
        PlayerCredential playerCredentials = registrationRequest.getPlayerCredential();
        // Step 1. Fetch associated player credentials
        PlayerCredential fetchedCredentials = playerCredentialRepository.findByEmail(playerCredentials.getEmail());
        // Step 2. If there is such credentials, than user already registered
        if (fetchedCredentials != null) {
            // Step 2.1 If the password is the same, just return identity to the user
            if (playerCredentials.getPassword().equals(fetchedCredentials.getPassword())) {
                return playerIdentityRepository.findOne(fetchedCredentials.getPlayerId());
            } else {
                // Step 2.2 If password does not match this is an error
                throw GogomayaException.fromError(GogomayaError.EmailAlreadyRegistered);
            }
        }
        // Step 3. Create new profile
        PlayerProfile playerProfile = registrationRequest.getPlayerProfile();
        playerProfile = playerProfileRepository.saveAndFlush(playerProfile);
        // Step 4. Create new credentials
        playerCredentials.setPlayerId(playerProfile.getPlayerId());
        playerCredentials = playerCredentialRepository.saveAndFlush(playerCredentials);
        // Step 5. Create new identity
        PlayerIdentity playerIdentity = new PlayerIdentity().setPlayerId(playerProfile.getPlayerId()).setSecret(UUID.randomUUID().toString());
        playerIdentity = playerIdentityRepository.saveAndFlush(playerIdentity);
        // Step 6. Registering in a Wallet registrar
        playerAccountService.register(playerProfile);
        // Step 7. Sending player identity
        return playerIdentity;
    }

}
