package com.clemble.casino.goal.spring;

import com.clemble.casino.goal.action.GoalManagerFactoryFacade;
import com.clemble.casino.goal.aspect.GenericGoalAspectFactory;
import com.clemble.casino.goal.aspect.ShortGoalAspectFactory;
import com.clemble.casino.goal.aspect.notification.GoalPlayerNotificationAspectFactory;
import com.clemble.casino.goal.aspect.outcome.ShortGoalLostOutcomeAspectFactory;
import com.clemble.casino.goal.aspect.outcome.ShortGoalWonOutcomeAspectFactory;
import com.clemble.casino.goal.aspect.persistence.ShortGoalStatePersistenceAspectFactory;
import com.clemble.casino.goal.aspect.record.GoalRecordAspectFactory;
import com.clemble.casino.goal.aspect.reminder.ReminderRuleAspectFactory;
import com.clemble.casino.goal.aspect.time.ShortGoalTimeAspectFactory;
import com.clemble.casino.goal.controller.GoalActionServiceController;
import com.clemble.casino.goal.controller.GoalRecordServiceController;
import com.clemble.casino.goal.controller.FriendGoalServiceController;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.GoalRoleConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalRole;
import com.clemble.casino.goal.listener.SystemGoalStartedEventListener;
import com.clemble.casino.goal.listener.SystemGoalTimeoutEventListener;
import com.clemble.casino.goal.repository.GoalRecordRepository;
import com.clemble.casino.goal.repository.ShortGoalStateRepository;
import com.clemble.casino.goal.service.EmailReminderService;
import com.clemble.casino.goal.service.PhoneReminderService;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.server.action.ClembleManagerFactory;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.*;
import com.google.common.collect.ImmutableSet;
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
    MongoSpringConfiguration.class
})
public class GoalManagementSpringConfiguration implements SpringConfiguration {

    @Bean
    public GoalRecordRepository goalRecordRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(GoalRecordRepository.class);
    }

    @Bean
    public ShortGoalStateRepository goalStateRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(ShortGoalStateRepository.class);
    }

    @Bean
    public GoalRecordServiceController goalRecordServiceController(GoalRecordRepository recordRepository) {
        return new GoalRecordServiceController(recordRepository);
    }

    @Bean
    public GoalActionServiceController goalActionServiceController(
        GoalManagerFactoryFacade factoryFacade,
        ShortGoalStateRepository shortGoalStateRepository) {
        return new GoalActionServiceController(factoryFacade, shortGoalStateRepository);
    }

    @Bean
    public EmailReminderService emailReminderService(SystemNotificationService systemNotificationService) {
        return new EmailReminderService(systemNotificationService);
    }

    @Bean
    public ReminderRuleAspectFactory heroEmailReminderRuleAspectFactory(EmailReminderService emailReminderService) {
        return new ReminderRuleAspectFactory(
            Ordered.HIGHEST_PRECEDENCE + 4,
            emailReminderService,
            (state) -> ImmutableSet.<String>of(state.getPlayer()),
            (configuration) -> configuration.getEmailReminderRule()
        );
    }

    @Bean
    public ReminderRuleAspectFactory supportEmailReminderRuleAspectFactory(EmailReminderService emailReminderService) {
        return new ReminderRuleAspectFactory(
            Ordered.HIGHEST_PRECEDENCE + 5,
            emailReminderService,
            (state) -> state.getSupporters(),
            (configuration) -> {
                GoalRoleConfiguration roleConfiguration = configuration.getRoleConfiguration(GoalRole.supporter);
                return roleConfiguration != null ? roleConfiguration.getEmailReminderRule() : null;
            }
        );
    }

    @Bean
    public PhoneReminderService phoneReminderService(SystemNotificationService systemNotificationService) {
        return new PhoneReminderService(systemNotificationService);
    }

    @Bean
    public ReminderRuleAspectFactory heroPhoneReminderRuleAspectFactory(PhoneReminderService reminderService) {
        return new ReminderRuleAspectFactory(
            Ordered.HIGHEST_PRECEDENCE + 6,
            reminderService,
            (state) -> ImmutableSet.<String>of(state.getPlayer()),
            (configuration) -> configuration.getPhoneReminderRule()
        );
    }

    @Bean
    public ReminderRuleAspectFactory supportPhoneReminderRuleAspectFactory(PhoneReminderService reminderService) {
        return new ReminderRuleAspectFactory(
            Ordered.HIGHEST_PRECEDENCE + 7,
            reminderService,
            (state) -> state.getSupporters(),
            (configuration) -> {
                GoalRoleConfiguration roleConfiguration = configuration.getRoleConfiguration(GoalRole.supporter);
                return roleConfiguration != null ? roleConfiguration.getPhoneReminderRule() : null;
            }
        );
    }

    @Bean
    public FriendGoalServiceController timelineServiceController(
        ShortGoalStateRepository shortGoalStateRepository,
        @Qualifier("playerConnectionClient") PlayerConnectionService playerConnectionClient
    ) {
        return new FriendGoalServiceController(shortGoalStateRepository, playerConnectionClient);
    }

    @Bean
    public ClembleManagerFactory<GoalConfiguration> shortGoalManagerFactory() {
        return new ClembleManagerFactory<>(GenericGoalAspectFactory.class, ShortGoalAspectFactory.class);
    }

    @Bean
    public GoalManagerFactoryFacade goalManagerFactoryFacade(
        @Qualifier("shortGoalManagerFactory") ClembleManagerFactory<GoalConfiguration> shortGoalManagerFactory,
        GoalRecordRepository recordRepository,
        ShortGoalStateRepository shortGoalStateRepository,
        @Qualifier("playerNotificationService") ServerNotificationService notificationService) {
        return new GoalManagerFactoryFacade(shortGoalManagerFactory, recordRepository, shortGoalStateRepository, notificationService);
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
    public GoalRecordAspectFactory goalRecordAspectFactory(
        GoalRecordRepository recordRepository,
        @Qualifier("playerNotificationService") ServerNotificationService notificationService){
        return new GoalRecordAspectFactory(recordRepository, notificationService);
    }

    @Bean
    public ShortGoalTimeAspectFactory goalTimeAspectFactory(SystemNotificationService systemNotificationService){
        return new ShortGoalTimeAspectFactory(systemNotificationService);
    }

    @Bean
    public ShortGoalStatePersistenceAspectFactory goalPersistenceAspectFactory(ShortGoalStateRepository stateRepository) {
        return new ShortGoalStatePersistenceAspectFactory(stateRepository);
    }

    @Bean
    public ShortGoalLostOutcomeAspectFactory goalMissedOutcomeAspectFactory(SystemNotificationService systemNotificationService) {
        return new ShortGoalLostOutcomeAspectFactory(systemNotificationService);
    }

    @Bean
    public ShortGoalWonOutcomeAspectFactory goalReachedOutcomeAspectFactory(SystemNotificationService systemNotificationService) {
        return new ShortGoalWonOutcomeAspectFactory(systemNotificationService);
    }

    @Bean
    public GoalPlayerNotificationAspectFactory goalPlayerNotificationAspectFactory(
        @Qualifier("playerNotificationService") ServerNotificationService notificationService) {
        return new GoalPlayerNotificationAspectFactory(notificationService);
    }

}
