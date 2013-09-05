package com.gogomaya.server.spring.web.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.error.GogomayaValidationService;
import com.gogomaya.server.player.account.PlayerAccountServerService;
import com.gogomaya.server.player.registration.PlayerProfileRegistrationServerService;
import com.gogomaya.server.repository.player.PlayerCredentialRepository;
import com.gogomaya.server.repository.player.PlayerIdentityRepository;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.management.ManagementCommonSpringConfiguration;
import com.gogomaya.server.spring.web.WebCommonSpringConfiguration;
import com.gogomaya.server.web.management.PlayerRegistrationController;

@Configuration
@Import(value = { ManagementCommonSpringConfiguration.class, WebCommonSpringConfiguration.class })
public class ManagementWebSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("playerCredentialRepository")
    public PlayerCredentialRepository playerCredentialRepository;;

    @Autowired
    @Qualifier("playerIdentityRepository")
    public PlayerIdentityRepository playerIdentityRepository;

    @Autowired
    @Qualifier("playerProfileRegistrationService")
    public PlayerProfileRegistrationServerService playerProfileRegistrationService;

    @Autowired
    @Qualifier("gogomayaValidationService")
    public GogomayaValidationService validationService;

    @Autowired
    @Qualifier("playerAccountService")
    public PlayerAccountServerService playerAccountService;

    @Bean
    public PlayerRegistrationController playerRegistrationController() {
        return new PlayerRegistrationController(playerProfileRegistrationService, playerCredentialRepository, playerIdentityRepository, validationService,
                playerAccountService);
    }
}
