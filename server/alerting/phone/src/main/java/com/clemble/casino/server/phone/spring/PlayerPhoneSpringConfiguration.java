package com.clemble.casino.server.phone.spring;

import com.clemble.casino.server.phone.controller.PlayerPhoneServiceController;
import com.clemble.casino.server.phone.listener.SystemPhoneSMSRequestEventListener;
import com.clemble.casino.server.phone.repository.PendingPlayerPhoneRepository;
import com.clemble.casino.server.phone.repository.PlayerPhoneRepository;
import com.clemble.casino.server.phone.service.PhoneCodeGenerator;
import com.clemble.casino.server.phone.service.ServerPlayerPhoneService;
import com.clemble.casino.server.phone.service.ServerSMSSender;
import com.clemble.casino.server.phone.service.TwilioSMSSender;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.template.MustacheTemplateService;
import com.twilio.sdk.TwilioRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * Created by mavarazy on 12/8/14.
 */
@Configuration
@Import({ CommonSpringConfiguration.class, MongoSpringConfiguration.class, PlayerPhoneSpringConfiguration.Cloud.class, PlayerPhoneSpringConfiguration.Default.class })
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
    public PlayerPhoneServiceController playerPhoneServiceController(ServerPlayerPhoneService serverPlayerPhoneService) {
        return new PlayerPhoneServiceController(serverPlayerPhoneService);
    }

    @Bean
    public ServerPlayerPhoneService serverPlayerPhoneService(
        PhoneCodeGenerator phoneCodeGenerator,
        ServerSMSSender serverSMSSender,
        PlayerPhoneRepository phoneRepository,
        PendingPlayerPhoneRepository pendingPlayerPhoneRepository,
        SystemNotificationService systemNotificationService) {
        return new ServerPlayerPhoneService(phoneCodeGenerator, serverSMSSender, phoneRepository, pendingPlayerPhoneRepository, systemNotificationService);
    }

    @Bean
    public SystemPhoneSMSRequestEventListener systemPhoneSMSRequestEventListener(
        ServerSMSSender smsSender,
        PlayerPhoneRepository phoneRepository,
        SystemNotificationServiceListener notificationServiceListener) {
        SystemPhoneSMSRequestEventListener smsSendEventListener = new SystemPhoneSMSRequestEventListener(new MustacheTemplateService(), smsSender, phoneRepository);
        notificationServiceListener.subscribe(smsSendEventListener);
        return smsSendEventListener;
    }

    @Configuration
    @Profile({SpringConfiguration.TEST, SpringConfiguration.DEFAULT, SpringConfiguration.INTEGRATION_TEST})
    public static class Default implements SpringConfiguration {

        @Bean
        public PhoneCodeGenerator phoneCodeGenerator() {
            return PhoneCodeGenerator.noPhoneCodeGenerator();
        }

        @Bean
        public ServerSMSSender serverSMSSender() {
            return new ServerSMSSender() {
                @Override
                public void send(String phone, String sms) {
                }
            };
        }

    }

    @Configuration
    @Profile({SpringConfiguration.CLOUD})
    public static class Cloud implements SpringConfiguration {

        @Bean
        public PhoneCodeGenerator phoneCodeGenerator() {
            return PhoneCodeGenerator.randomPhoneCodeGenerator();
        }

        @Bean
        public TwilioRestClient twilioRestClient(
            @Value("${clemble.phone.twilio.sid}") String accountSid,
            @Value("${clemble.phone.twilio.token}") String authToken) {
            return new TwilioRestClient(accountSid, authToken);
        }

        @Bean
        public TwilioSMSSender serverSMSSender(TwilioRestClient restClient) {
            return new TwilioSMSSender(restClient);
        }

    }

}
