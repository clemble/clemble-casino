package com.clemble.casino.server.web.management;

import static com.clemble.casino.web.management.ManagementWebMapping.MANAGEMENT_PLAYER_LOGIN;
import static com.clemble.casino.web.management.ManagementWebMapping.MANAGEMENT_PLAYER_REGISTRATION;
import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.player.service.PlayerRegistrationService;
import com.clemble.casino.server.event.SystemPlayerProfileRegistered;
import com.clemble.casino.server.security.ClembleConsumerDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.player.web.PlayerLoginRequest;
import com.clemble.casino.player.web.PlayerRegistrationRequest;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.player.PlayerIdGenerator;
import com.clemble.casino.server.player.presence.SystemNotificationService;
import com.clemble.casino.server.player.security.PlayerTokenFactory;
import com.clemble.casino.server.repository.player.PlayerCredentialRepository;
import com.clemble.casino.web.mapping.WebMapping;

@Controller
public class PlayerRegistrationController implements PlayerRegistrationService, ExternalController {
    // !!!TODO need a safe restoration process for all Registrations not only for login!!!

    final private PlayerIdGenerator playerIdentifierGenerator;
    final private PlayerTokenFactory playerTokenFactory;
    final private PlayerCredentialRepository playerCredentialRepository;
    final private SystemNotificationService notificationService;
    final private ClembleCasinoValidationService validationService;
    final private ClembleConsumerDetailsService consumerDetailsService;

    public PlayerRegistrationController(final PlayerIdGenerator playerIdentifierGenerator,
            final PlayerTokenFactory playerTokenFactory,
            final PlayerCredentialRepository playerCredentialRepository,
            final ClembleConsumerDetailsService playerIdentityRepository,
            final ClembleCasinoValidationService validationService,
            final SystemNotificationService notificationService) {
        this.playerIdentifierGenerator = checkNotNull(playerIdentifierGenerator);
        this.playerTokenFactory = checkNotNull(playerTokenFactory);
        this.playerCredentialRepository = checkNotNull(playerCredentialRepository);
        this.consumerDetailsService = checkNotNull(playerIdentityRepository);
        this.validationService = checkNotNull(validationService);
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = MANAGEMENT_PLAYER_LOGIN, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody PlayerToken login(@RequestBody PlayerLoginRequest loginRequest) {
        PlayerCredential playerCredentials = loginRequest.getPlayerCredential();
        // Step 1. Fetch saved player credentials
        PlayerCredential fetchedCredentials = playerCredentialRepository.findByEmail(playerCredentials.getEmail());
        // Step 2. If there is no such credentials, than user is unregistered
        if (fetchedCredentials == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.EmailOrPasswordIncorrect);
        // Step 3. Compare passwords
        if (!fetchedCredentials.getPassword().equals(playerCredentials.getPassword()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.EmailOrPasswordIncorrect);
        // Step 4. Everything is fine, return Identity
        consumerDetailsService.save(loginRequest.getConsumerDetails());
        return playerTokenFactory.create(fetchedCredentials.getPlayer(), loginRequest.getConsumerDetails());
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = MANAGEMENT_PLAYER_REGISTRATION, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    PlayerToken createPlayer(@RequestBody final PlayerRegistrationRequest registrationRequest) {
        // Step 1. Validating input data prior to any actions
        PlayerToken playerIdentity = restoreUser(registrationRequest);
        if (playerIdentity != null)
            return playerIdentity;
        validationService.validate(registrationRequest.getPlayerCredential());
        validationService.validate(registrationRequest.getPlayerProfile());
        validationService.validate(registrationRequest.getConsumerDetails());
        // Step 2. Creating appropriate PlayerProfile
        String player = playerIdentifierGenerator.newId();
        // Step 3. Adding initial fields to PlayerProfile
        if (registrationRequest.getPlayerProfile() == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.ProfileInvalid);
        if (registrationRequest.getPlayerProfile().getSocialConnections() != null && !registrationRequest.getPlayerProfile().getSocialConnections().isEmpty())
            throw ClembleCasinoException.fromError(ClembleCasinoError.ProfileSocialMustBeEmpty);
        PlayerProfile normalizedProfile = registrationRequest.getPlayerProfile();
        normalizedProfile.setPlayer(player);
        if(normalizedProfile.getNickName() == null) {
            String email = registrationRequest.getPlayerCredential().getEmail();
            normalizedProfile.setNickName(email.substring(0, email.indexOf("@")));
        }
        validationService.validate(normalizedProfile);
        // Step 4. Registration done through separate registration service
        PlayerToken token = register(registrationRequest, player);
        // Step 5. Notifying system of new user
        notificationService.notify(new SystemPlayerProfileRegistered(player, normalizedProfile));
        // Step 6. All done returning response
        return token;
    }

    private PlayerToken restoreUser(PlayerLoginRequest loginRequest) {
        // Step 1. Checking request is valid
        validationService.validate(loginRequest);
        // Step 2. Checking this is a new user or an old one
        PlayerCredential playerCredentials = loginRequest.getPlayerCredential();
        // Step 3. Fetch associated player credentials
        PlayerCredential fetchedCredentials = playerCredentialRepository.findByEmail(playerCredentials.getEmail());
        // Step 2. If there is such credentials, than user already registered
        if (fetchedCredentials != null) {
            // Step 2.1 If the password is the same, just return identity to the user
            if (playerCredentials.getPassword().equals(fetchedCredentials.getPassword())) {
                return playerTokenFactory.create(fetchedCredentials.getPlayer(), loginRequest.getConsumerDetails());
            } else {
                // Step 2.2 If password does not match this is an error
                throw ClembleCasinoException.fromError(ClembleCasinoError.EmailAlreadyRegistered);
            }
        }
        return null;
    }

    public PlayerToken register(final PlayerLoginRequest loginRequest, final String player) {
        validationService.validate(loginRequest);
        validationService.validate(loginRequest.getPlayerCredential());
        validationService.validate(loginRequest.getConsumerDetails());
        // Step 1. Create new credentials
        PlayerCredential playerCredentials = loginRequest.getPlayerCredential().setPlayer(player);
        playerCredentials = playerCredentialRepository.save(playerCredentials);
        // Step 2. Specifying player type
        // TODO move to registration listener playerProfile.setType(PlayerType.free);
        // Step 3. Create new token
        PlayerToken playerToken = playerTokenFactory.create(player, loginRequest.getConsumerDetails());
        // Step 4. Returning generated player token
        return playerToken;
    }

}
