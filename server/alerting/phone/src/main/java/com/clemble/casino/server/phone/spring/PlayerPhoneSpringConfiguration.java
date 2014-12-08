package com.clemble.casino.server.phone.spring;

import com.clemble.casino.server.phone.repository.PendingPlayerPhoneRepository;
import com.clemble.casino.server.phone.repository.PlayerPhoneRepository;
import com.clemble.casino.server.phone.service.TwilioSMSSender;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.twilio.sdk.TwilioRestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * Created by mavarazy on 12/8/14.
 */
@Configuration
@Import({CommonSpringConfiguration.class, MongoSpringConfiguration.class})
public class PlayerPhoneSpringConfiguration implements SpringConfiguration {

    @Bean
    public PlayerPhoneRepository playerPhoneRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(PlayerPhoneRepository.class);
    }

    @Bean
    public PendingPlayerPhoneRepository pendingPlayerPhoneRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(PendingPlayerPhoneRepository.class);
    }

    @Bean
    public TwilioRestClient twilioRestClient() {
        return new TwilioRestClient("ACa413250fe57e48dbf4ebfc5ba175f8e2", "e0306dc7a720d0962f2d5b5f9c00eb98");
    }

    @Bean
    public TwilioSMSSender twillioSMSSender(TwilioRestClient restClient){
        return new TwilioSMSSender(restClient);
    }

}
