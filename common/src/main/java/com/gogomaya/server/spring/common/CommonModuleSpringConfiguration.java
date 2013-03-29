package com.gogomaya.server.spring.common;

import javax.inject.Singleton;
import javax.validation.Validation;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import com.gogomaya.server.error.GogomayaValidationService;

@Configuration
@Import({ CommonModuleSpringConfiguration.CommonModuleCloudFoundryConfigurations.class, RabbitSpringConfiguration.class,
        JPASpringConfiguration.class, RedisSpringConfiguration.class })
public class CommonModuleSpringConfiguration {

    @Bean @Singleton
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }
    
    @Bean @Singleton
    public JsonMessageConverter jsonMessageConverter(){
        JsonMessageConverter jsonMessageConverter = new JsonMessageConverter();
        jsonMessageConverter.setJsonObjectMapper(objectMapper());
        return jsonMessageConverter;
    }

    @Bean @Singleton
    public GogomayaValidationService validationService() {
        return new GogomayaValidationService(Validation.buildDefaultValidatorFactory());
    }

    @Configuration
    @Profile(value = "cloud")
    public static class CommonModuleCloudFoundryConfigurations {

        @Bean @Singleton
        public CloudEnvironment cloudEnvironment() {
            return new CloudEnvironment();
        }

    }

}
