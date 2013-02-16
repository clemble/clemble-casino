package com.gogomaya.server.spring.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.server.spring.player.PlayerManagementSpringConfiguration;
import com.gogomaya.server.spring.social.SocialModuleSpringConfiguration;

@Configuration
@Import(value = { PlayerManagementSpringConfiguration.class, SocialModuleSpringConfiguration.class })
public class WebGenericConfiguration {

}
