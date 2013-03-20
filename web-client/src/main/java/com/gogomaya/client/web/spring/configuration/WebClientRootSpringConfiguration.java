package com.gogomaya.client.web.spring.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.gogomaya.client.web", excludeFilters = { @Filter(Configuration.class) })
public class WebClientRootSpringConfiguration {

}
