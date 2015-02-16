package com.clemble.casino.server.spring.common;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.springframework.context.annotation.*;

import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.server.player.lock.JavaPlayerLockService;
import com.clemble.casino.server.player.lock.PlayerLockService;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
@Import({
    PropertiesSpringConfiguration.class,
    RabbitSpringConfiguration.class,
    SystemNotificationSpringConfiguration.class})
public class CommonSpringConfiguration implements SpringConfiguration {

    @Bean
    public PlayerLockService playerLockService() {
        return new JavaPlayerLockService();
    }

    @Bean
    public ClembleCasinoValidationService clembleValidationService(ValidatorFactory validatorFactory) {
        return new ClembleCasinoValidationService(validatorFactory);
    }

    @Bean
    public ValidatorFactory validatorFactory(){
        return Validation.buildDefaultValidatorFactory();
    }

    @Bean
    public Validator springValidator(ValidatorFactory validatorFactory) {
        CustomValidatorBean factoryBean = new CustomValidatorBean();
        factoryBean.setValidatorFactory(validatorFactory);
        return factoryBean;
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(ValidatorFactory validatorFactory) {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
        methodValidationPostProcessor.setValidatorFactory(validatorFactory);
        methodValidationPostProcessor.setProxyTargetClass(true);
        return methodValidationPostProcessor;
    }

}
