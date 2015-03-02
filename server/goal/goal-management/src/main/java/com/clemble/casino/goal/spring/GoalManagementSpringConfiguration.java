package com.clemble.casino.goal.spring;

import com.clemble.casino.goal.action.GoalManagerFactoryFacade;
import com.clemble.casino.goal.aspect.GenericGoalAspectFactory;
import com.clemble.casino.goal.aspect.ShortGoalAspectFactory;
import com.clemble.casino.goal.aspect.bet.GoalBetPaymentAspectFactory;
import com.clemble.casino.goal.aspect.bet.GoalBetOffAspectFactory;
import com.clemble.casino.goal.aspect.bet.GoalBetRuleAspectFactory;
import com.clemble.casino.goal.aspect.notification.GoalPlayerNotificationAspectFactory;
import com.clemble.casino.goal.aspect.notification.SystemGoalReachedNotificationAspectFactory;
import com.clemble.casino.goal.aspect.outcome.GoalLostOutcomeAspectFactory;
import com.clemble.casino.goal.aspect.outcome.GoalWonOutcomeAspectFactory;
import com.clemble.casino.goal.aspect.persistence.GoalStatePersistenceAspectFactory;
import com.clemble.casino.goal.aspect.record.GoalRecordAspectFactory;
import com.clemble.casino.goal.aspect.reminder.PlayerReminderRuleAspectFactory;
import com.clemble.casino.goal.aspect.reminder.SupporterReminderRuleAspectFactory;
import com.clemble.casino.goal.aspect.security.GoalSecurityAspectFactory;
import com.clemble.casino.goal.aspect.share.ShareRuleAspectFactory;
import com.clemble.casino.goal.aspect.timeout.GoalTimeoutAspectFactory;
import com.clemble.casino.goal.controller.GoalActionServiceController;
import com.clemble.casino.goal.controller.GoalRecordServiceController;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.GoalRoleConfiguration;
import com.clemble.casino.goal.listener.SystemGoalBetOffEventListener;
import com.clemble.casino.goal.listener.SystemGoalStartedEventListener;
import com.clemble.casino.goal.listener.SystemGoalTimeoutEventListener;
import com.clemble.casino.goal.repository.GoalRecordRepository;
import com.clemble.casino.goal.repository.GoalStateRepository;
import com.clemble.casino.goal.service.EmailReminderService;
import com.clemble.casino.goal.service.PhoneReminderService;
import com.clemble.casino.payment.service.PlayerAccountService;
import com.clemble.casino.server.action.ClembleManagerFactory;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.*;
import com.clemble.casino.social.SocialProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * Created by mavarazy on 9/12/14.
 */
@Configuration
@Import({
    CommonSpringConfiguration.class,
    MongoSpringConfiguration.class,
    PaymentClientSpringConfiguration.class
})
public class GoalManagementSpringConfiguration implements SpringConfiguration {

    @Bean
    public GoalRecordRepository goalRecordRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(GoalRecordRepository.class);
    }

    @Bean
    public GoalStateRepository goalStateRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(GoalStateRepository.class);
    }

    @Bean
    public GoalRecordServiceController goalRecordServiceController(GoalRecordRepository recordRepository) {
        return new GoalRecordServiceController(recordRepository);
    }

    @Bean
    public GoalActionServiceController goalActionServiceController(
        GoalManagerFactoryFacade factoryFacade,
        GoalStateRepository goalStateRepository) {
        return new GoalActionServiceController(factoryFacade, goalStateRepository);
    }

    @Bean
    public EmailReminderService emailReminderService(SystemNotificationService systemNotificationService) {
        return new EmailReminderService(systemNotificationService);
    }

    @Bean
    public PlayerReminderRuleAspectFactory heroEmailReminderRuleAspectFactory(EmailReminderService emailReminderService) {
        return new PlayerReminderRuleAspectFactory(
            Ordered.HIGHEST_PRECEDENCE + 4,
            emailReminderService,
            (configuration) -> configuration.getEmailReminderRule()
        );
    }

    @Bean
    public SupporterReminderRuleAspectFactory supportEmailReminderRuleAspectFactory(EmailReminderService emailReminderService) {
        return new SupporterReminderRuleAspectFactory(
            Ordered.HIGHEST_PRECEDENCE + 5,
            emailReminderService,
            (configuration) -> {
                GoalRoleConfiguration roleConfiguration = configuration.getSupporterConfiguration();
                return roleConfiguration != null ? roleConfiguration.getEmailReminderRule() : null;
            }
        );
    }

    @Bean
    public PhoneReminderService phoneReminderService(SystemNotificationService systemNotificationService) {
        return new PhoneReminderService(systemNotificationService);
    }

    @Bean
    public PlayerReminderRuleAspectFactory heroPhoneReminderRuleAspectFactory(PhoneReminderService reminderService) {
        return new PlayerReminderRuleAspectFactory(
            Ordered.HIGHEST_PRECEDENCE + 7,
            reminderService,
            (configuration) -> configuration.getPhoneReminderRule()
        );
    }

    @Bean
    public GoalSecurityAspectFactory goalSecurityAspectFactory() {
        return new GoalSecurityAspectFactory();
    }

    @Bean
    public GoalBetPaymentAspectFactory goalBetAspectFactory(
        @Qualifier("playerAccountClient") PlayerAccountService accountService,
        SystemNotificationService notificationService) {
        return new GoalBetPaymentAspectFactory(accountService, notificationService);
    }

    @Bean
    public ShareRuleAspectFactory twitterShareRuleAspectFactory(SystemNotificationService notificationService) {
        return new ShareRuleAspectFactory(SocialProvider.twitter, notificationService, Ordered.LOWEST_PRECEDENCE - 101);
    }

    @Bean
    public ShareRuleAspectFactory facebookShareRuleAspectFactory(SystemNotificationService notificationService) {
        return new ShareRuleAspectFactory(SocialProvider.facebook, notificationService, Ordered.LOWEST_PRECEDENCE - 100);
    }

    @Bean
    public SupporterReminderRuleAspectFactory supportPhoneReminderRuleAspectFactory(PhoneReminderService reminderService) {
        return new SupporterReminderRuleAspectFactory(
            Ordered.HIGHEST_PRECEDENCE + 8,
            reminderService,
            (configuration) -> {
                GoalRoleConfiguration roleConfiguration = configuration.getSupporterConfiguration();
                return roleConfiguration != null ? roleConfiguration.getPhoneReminderRule() : null;
            }
        );
    }

    @Bean
    public ClembleManagerFactory<GoalConfiguration> shortGoalManagerFactory() {
        return new ClembleManagerFactory<>(GenericGoalAspectFactory.class, ShortGoalAspectFactory.class);
    }

    @Bean
    public GoalManagerFactoryFacade goalManagerFactoryFacade(
        @Qualifier("shortGoalManagerFactory") ClembleManagerFactory<GoalConfiguration> shortGoalManagerFactory,
        GoalRecordRepository recordRepository,
        GoalStateRepository goalStateRepository,
        @Qualifier("playerNotificationService") ServerNotificationService notificationService) {
        return new GoalManagerFactoryFacade(shortGoalManagerFactory, recordRepository, goalStateRepository, notificationService);
    }

    @Bean
    public SystemGoalStartedEventListener systemGoalStartedEventListener(
        @Qualifier("playerNotificationService") ServerNotificationService notificationService,
        SystemNotificationServiceListener notificationServiceListener,
        GoalManagerFactoryFacade goalManagerFactoryFacade) {
        SystemGoalStartedEventListener eventListener = new SystemGoalStartedEventListener(notificationService, goalManagerFactoryFacade);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Bean
    public SystemGoalTimeoutEventListener systemGoalTimeoutEventListener(
        SystemNotificationServiceListener notificationServiceListener,
        GoalManagerFactoryFacade goalManagerFactoryFacade) {
        SystemGoalTimeoutEventListener eventListener = new SystemGoalTimeoutEventListener(goalManagerFactoryFacade);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Bean
    public SystemGoalBetOffEventListener systemGoalForbidBetEventListener(
        SystemNotificationServiceListener notificationServiceListener,
        GoalManagerFactoryFacade goalManagerFactoryFacade) {
        SystemGoalBetOffEventListener eventListener = new SystemGoalBetOffEventListener(goalManagerFactoryFacade);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Bean
    public GoalRecordAspectFactory goalRecordAspectFactory(
        GoalRecordRepository recordRepository,
        @Qualifier("playerNotificationService") ServerNotificationService notificationService){
        return new GoalRecordAspectFactory(recordRepository, notificationService);
    }

    @Bean
    public GoalTimeoutAspectFactory goalTimeAspectFactory(SystemNotificationService systemNotificationService){
        return new GoalTimeoutAspectFactory(systemNotificationService);
    }

    @Bean
    public GoalStatePersistenceAspectFactory goalPersistenceAspectFactory(GoalStateRepository stateRepository) {
        return new GoalStatePersistenceAspectFactory(stateRepository);
    }

    @Bean
    public GoalLostOutcomeAspectFactory goalMissedOutcomeAspectFactory(SystemNotificationService systemNotificationService) {
        return new GoalLostOutcomeAspectFactory(systemNotificationService);
    }

    @Bean
    public GoalWonOutcomeAspectFactory goalReachedOutcomeAspectFactory(SystemNotificationService systemNotificationService) {
        return new GoalWonOutcomeAspectFactory(systemNotificationService);
    }

    @Bean
    public GoalBetRuleAspectFactory goalBetRuleAspectFactory() {
        return new GoalBetRuleAspectFactory();
    }

    @Bean
    public GoalPlayerNotificationAspectFactory goalPlayerNotificationAspectFactory(
        @Qualifier("playerNotificationService") ServerNotificationService notificationService) {
        return new GoalPlayerNotificationAspectFactory(notificationService);
    }

    @Bean
    public GoalBetOffAspectFactory goalBetForbidAspectFactory(
        SystemNotificationService notificationService) {
        return new GoalBetOffAspectFactory(notificationService);
    }

    @Bean
    public SystemGoalReachedNotificationAspectFactory systemGoalReachedNotificationAspectFactory(SystemNotificationService notificationService) {
        return new SystemGoalReachedNotificationAspectFactory(notificationService);
    }

}
