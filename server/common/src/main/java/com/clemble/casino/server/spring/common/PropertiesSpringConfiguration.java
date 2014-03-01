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
    @PropertySource(value = "file:/clemble/config/default.properties")
    @Profile(value = { DEFAULT })
    public static class Default {
    }

    @Configuration
    @PropertySource(value = "file:/clemble/config/test.properties")
    @Profile(value = {TEST})
    public static class Test {
    }

    @Configuration
    @Profile(value = { CLOUD })
    @PropertySource(value = "file:/clemble/config/cloud.properties")
    public static class Cloud {
    }
}
