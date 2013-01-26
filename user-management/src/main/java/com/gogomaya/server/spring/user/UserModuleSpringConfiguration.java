package com.gogomaya.server.spring.user;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.server.spring.common.CommonModuleSpringConfiguration;

@Configuration
@Import(value = { CommonModuleSpringConfiguration.class, JPAConfigurations.class })
public class UserModuleSpringConfiguration {

}
