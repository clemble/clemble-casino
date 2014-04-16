package com.clemble.casino.server.spring.common;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@Import({PropertiesSpringConfiguration.Default.class, PropertiesSpringConfiguration.Test.class, PropertiesSpringConfiguration.Cloud.class})
public class PropertiesSpringConfiguration implements SpringConfiguration {

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Configuration
    @PropertySource(name = "clembleConfig", value = "file:${user.home}/clemble/config/default.properties")
    @Profile(value = { DEFAULT, INTEGRATION_TEST, INTEGRATION_DEFAULT })
    public static class Default {
    }

    @Configuration
    @PropertySource(name = "clembleConfig", value = "file:${user.home}/clemble/config/test.properties")
    @Profile(value = {TEST})
    public static class Test {
    }

    @Configuration
    @Profile(value = { CLOUD, INTEGRATION_CLOUD })
    @PropertySource(name = "clembleConfig", value = "file:${user.home}/clemble/config/cloud.properties")
    public static class Cloud {
    }
}
