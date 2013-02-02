package com.gogomaya.server.spring.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.server.spring.social.SocialModuleSpringConfiguration;
import com.gogomaya.server.spring.user.UserModuleSpringConfiguration;

@Configuration
@Import(value = { UserModuleSpringConfiguration.class, SocialModuleSpringConfiguration.class })
public class WebGenericConfiguration {

}
