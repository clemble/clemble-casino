package com.clemble.casino.integration.player.profile;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import org.springframework.web.client.RestTemplate;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.player.security.PlayerSession;
import com.clemble.casino.player.service.PlayerProfileService;

abstract public class PlayerProfileServiceFactory {

    abstract public PlayerProfileService construct(ClembleCasinoOperations player);

    public static class SingletonPlayerProfileServiceFactory extends PlayerProfileServiceFactory {

        final private PlayerProfileService playerProfileService;

        public SingletonPlayerProfileServiceFactory(PlayerProfileService playerProfileService) {
            this.playerProfileService = checkNotNull(playerProfileService);
        }

        @Override
        public PlayerProfileService construct(ClembleCasinoOperations player) {
            return playerProfileService;
        }
    }

    public static class IntegrationPlayerProfileServiceFactory extends PlayerProfileServiceFactory {

        final private RestTemplate restTemplate;

        public IntegrationPlayerProfileServiceFactory(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        @Override
        public PlayerProfileService construct(ClembleCasinoOperations player) {
            return new IntegrationPlayerProfileService(player.getHost(), restTemplate);
        }

    }
}
