package com.clemble.casino.android.player;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.android.ClembleCasinoConstants;
import com.clemble.casino.client.error.ClembleCasinoErrorHandler;
import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.player.service.PlayerRegistrationService;
import com.clemble.casino.player.web.PlayerLoginRequest;
import com.clemble.casino.player.web.PlayerRegistrationRequest;
import com.clemble.casino.player.web.PlayerSocialRegistrationRequest;
import com.clemble.casino.web.management.ManagementWebMapping;

public class AndroidPlayerRegistrationService implements PlayerRegistrationService {

    final private String managementUrl;
    final private RestTemplate restTemplate;

    public AndroidPlayerRegistrationService(String managementUrl) {
        this.managementUrl = checkNotNull(managementUrl);
        this.restTemplate = new RestTemplate();

        this.restTemplate.setErrorHandler(new ClembleCasinoErrorHandler(ClembleCasinoConstants.OBJECT_MAPPER));
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setObjectMapper(ClembleCasinoConstants.OBJECT_MAPPER);
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(jackson2HttpMessageConverter);

        this.restTemplate.setMessageConverters(messageConverters);
    }

    @Override
    public PlayerToken login(PlayerLoginRequest playerLoginRequest) {
        return restTemplate.postForEntity(managementUrl + ManagementWebMapping.MANAGEMENT_PLAYER_LOGIN, playerLoginRequest, PlayerToken.class).getBody();
    }

    @Override
    public PlayerToken createPlayer(PlayerRegistrationRequest registrationRequest) {
        return restTemplate.postForEntity(managementUrl + ManagementWebMapping.MANAGEMENT_PLAYER_REGISTRATION, registrationRequest, PlayerToken.class).getBody();
    }

    @Override
    public PlayerToken createSocialPlayer(PlayerSocialRegistrationRequest socialConnectionData) {
        return restTemplate.postForEntity(managementUrl + ManagementWebMapping.MANAGEMENT_PLAYER_REGISTRATION_SOCIAL, socialConnectionData, PlayerToken.class).getBody();
    }

}
